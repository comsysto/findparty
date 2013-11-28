package com.comsysto.findbuddies.android.activity;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.menu.OptionMenuHandler;
import com.comsysto.findbuddies.android.model.CategoryType;
import com.comsysto.findbuddies.android.service.LocationInfo;
import com.comsysto.findbuddies.android.service.LocationRequester;
import com.comsysto.findbuddies.android.service.LocationService;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyAsync;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyCallback;
import com.comsysto.findbuddies.android.service.async.picture.GetPictureAsync;
import com.comsysto.findbuddies.android.service.async.picture.GetPictureCallback;
import com.comsysto.findbuddies.android.widget.LoadingProgressDialog;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Super class for {@link EditPartyActivity} and {@link CreatePartyActivity}.
 *
 * @author stefandjurasic
 */
public abstract class PartyActivity extends AbstractActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, Observer, LocationRequester, UpdatePartyCallback, GetPictureCallback {

    private OptionMenuHandler optionMenuHandler;
    TextView categoryNameText;
    Spinner levelSpinner;
    Spinner categorySpinner;

    SimpleDateFormat formatter = new SimpleDateFormat();
    private Button partyTime;
    static final String[] LEVELS = new String[]{"Beginner", "Amateur", "Professional"};
    Button numberOfParticipantsButton;
    private Button partyLocationButton;
    private Button subjectButton;
    LocationService locationService;
    Party party;
    private boolean alreadyCalled;

    LoadingProgressDialog loadingProgressDialog;
    private ImageView partyPicture;
    private Bitmap partyPictureBitmap;
    private ViewSwitcher viewSwitcher;

    void dismissProgressDialog() {
        if (loadingProgressDialog != null || loadingProgressDialog.isShowing()) {
            loadingProgressDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.loadingProgressDialog = new LoadingProgressDialog(this, getString(R.string.SAVING_ACTIVITY_SPINNER), false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.optionMenuHandler = new OptionMenuHandler(this);

        setContentView(R.layout.create_edit_party);

        this.party = getParty();

        initCategory();
        initLevelSpinner();
        initPartyTimeButton();
        initParticipantsButton();
        initPartyLocationButton();
        initSubjectButton();
        initPartyPicture();
    }

    private void initPartyPicture() {
        final String pictureUrl = party.getPictureUrl();
        this.viewSwitcher = (ViewSwitcher)findViewById(R.id.viewSwitcher);
        this.partyPicture = (ImageView)findViewById(R.id.partyPicture);

        final String textForOwnerPicture = getString(R.string.YOUR_PICTURE);
        final String textForOtherPicture = getString(R.string.OTHER_PICTURE);

        this.viewSwitcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PartyActivity.this);
                PartyPictureSelector listener = new PartyPictureSelector();
                builder.setSingleChoiceItems(new String[]{textForOwnerPicture, textForOtherPicture},
                        isGooglePicture(pictureUrl) ? 0 : 1, listener);
                builder.setPositiveButton(R.string.OK_BUTTON, listener);
                builder.show();
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        if (pictureUrl != null) {
            new GetPictureAsync(this, this.party.getPictureUrl()).execute();
        }
    }

    private class PartyPictureSelector implements DialogInterface.OnClickListener {

            private int selectedOption = -1;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == -1) {
                    if (selectedOption == -1) {
                        //do nothing, user didn't change selection
                    } else if (selectedOption == 1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        PartyActivity.this.startActivityForResult(intent, 0);
                    } else {
                        showProgressBar();
                        new GetPictureAsync(PartyActivity.this, PartyManagerApplication.getInstance().getUserImageUrl()).execute();
                    }
                } else {
                    if (party.getPictureUrl() == null ||
                            which == 0 && !isGooglePicture(party.getPictureUrl())
                            || which == 1 && isGooglePicture(party.getPictureUrl()))
                    selectedOption = which;
                }
        }
    }

    public boolean isGooglePicture(String pictureUrl) {
        if (pictureUrl == null) {
            return false;
        }
        return pictureUrl.contains("googleusercontent");
    }

    protected abstract Party getParty();

    private void initSubjectButton() {
        this.subjectButton = (Button) findViewById(R.id.addSubjectButton);

        this.subjectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment dialog = new DialogFragment() {

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                        View view = inflater.inflate(R.layout.subject_input, container);
                        getDialog().setTitle(R.string.SUBJECT_INPUT_TITLE);
                        final EditText subject = (EditText)view.findViewById(R.id.subjectEditText);

                        if(party.getSubject() != null){
                            subject.setText(party.getSubject());
                        }

                        Button cancelButton = (Button)view.findViewById(R.id.cancelSubjectInputButton);
                        cancelButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        });

                        final Button submitSubjectButton = (Button)view.findViewById(R.id.OK_BUTTON);
                        submitSubjectButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                party.setSubject(subject.getText().toString());
                                dismiss();
                            }
                        });
                        submitSubjectButton.setClickable(false);
                        submitSubjectButton.setEnabled(false);


                        subject.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                boolean clickable = subject.getText().toString().length() > 2;
                                submitSubjectButton.setClickable(clickable);
                                submitSubjectButton.setEnabled(clickable);
                            }
                        });

                        return view;
                    }
                };
                dialog.show(getFragmentManager(), "subjectInputFragment");
            }
        });

        if (party.getLocation() != null) {
            locationService.requestLocationFromPoint(party.getLocation(), this);
        } else {
            locationService.activate();
        }
    }

    private void initPartyLocationButton() {
        locationService = new LocationService(getApplicationContext(), this);
        this.partyLocationButton = (Button) findViewById(R.id.partyLocationButton);

        this.partyLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment dialog = new DialogFragment() {

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                        View view = inflater.inflate(R.layout.adress_input, container);
                        getDialog().setTitle(R.string.LOCATION_INPUT_TITLE);
                        final EditText address = (EditText)view.findViewById(R.id.addressEditText);

                        Button cancelButton = (Button)view.findViewById(R.id.cancelLocationInputButton);
                        cancelButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        });

                        Button activateLocationButtonViaGps = (Button)view.findViewById(R.id.activateLocationViaGpsButton);
                        activateLocationButtonViaGps.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                partyLocationButton.setText(R.string.LOCATION_BUTTON_WAITING_TEXT);
                                locationService.activate();
                                dismiss();
                            }
                        });

                        final Button submitLocationButton = (Button)view.findViewById(R.id.submitLocationInputButton);
                        submitLocationButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                locationService.requestPointFromAddress(address.getText().toString(), PartyActivity.this);
                                dismiss();
                            }
                        });
                        submitLocationButton.setClickable(false);
                        submitLocationButton.setEnabled(false);


                        address.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                boolean clickable = address.getText().toString().length() > 2;
                                submitLocationButton.setClickable(clickable);
                                submitLocationButton.setEnabled(clickable);
                            }
                        });

                        return view;
                    }
                };
                dialog.show(getFragmentManager(), "addressInputFragment");
            }
        });

        if (party.getLocation() != null) {
            locationService.requestLocationFromPoint(party.getLocation(), this);
        } else {
            locationService.activate();
        }
    }

    private void initParticipantsButton() {
        this.numberOfParticipantsButton = (Button) findViewById(R.id.numberOfParticipantsButton);
        this.numberOfParticipantsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final NumberPicker picker = new NumberPicker(getActivity());
                        picker.setMinValue(1);
                        picker.setMaxValue(100);
                        picker.setEnabled(true);
                        picker.setValue(party.getSize());
                        picker.setFormatter(new NumberPicker.Formatter() {
                            @Override
                            public String format(int value) {
                                return value + " Participants";
                            }
                        });
                        builder.setView(picker);
                        builder.setTitle(getResources().getString(R.string.NUMBER_OF_PARTICIPANTS_DIALOG_TITLE));
                        builder.setPositiveButton(R.string.OK_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int numberOfParticipants = picker.getValue();
                                    party.setSize(numberOfParticipants);
                                    setTextOnNumberOfParticipantsButton(numberOfParticipants);
                                } catch (NumberFormatException e) {
                                    //Ignore wrong entered numbers
                                }

                            }
                        });
                        return builder.create();
                    }


                };
                fragment.show(getFragmentManager(), "numberOfParticipantsPicker");

            }
        });
        setTextOnNumberOfParticipantsButton(party.getSize());
    }

    private void initPartyTimeButton() {
        this.partyTime = (Button) findViewById(R.id.partyTimeButton);

        partyTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        alreadyCalled = false;
                        Calendar calendar = getCalendarFromParty();
                        return new DatePickerDialog(getActivity(), PartyActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }
                };

                fragment.show(getFragmentManager(), "datePicker");
            }
        });
        setTimeOnView();
    }

    private Calendar getCalendarFromParty() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(party.getStartDate());
        return calendar;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendarFromParty = getCalendarFromParty();
        calendarFromParty.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendarFromParty.set(Calendar.MINUTE, minute);
        party.setStartDate(calendarFromParty.getTime());
        setTimeOnView();
    }

    void setTimeOnView() {
        partyTime.setText("On " + formatter.format(party.getStartDate()));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (alreadyCalled) {
            return;
        }
        alreadyCalled = true;

        DialogFragment fragment = new DialogFragment() {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                Calendar calendar = getCalendarFromParty();
                return new TimePickerDialog(getActivity(), PartyActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
            }
        };
        fragment.show(getFragmentManager(), "timePicker");
        Calendar calendarFromParty = getCalendarFromParty();

        calendarFromParty.set(Calendar.YEAR, year);
        calendarFromParty.set(Calendar.MONTH, monthOfYear);
        calendarFromParty.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        party.setStartDate(calendarFromParty.getTime());


    }


    private void initLevelSpinner() {
        levelSpinner = (Spinner) findViewById(R.id.levelSpinner);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.findbuddies_spinner, LEVELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        levelSpinner.setAdapter(adapter);
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                party.setLevel(adapter.getItem(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        levelSpinner.setSelection(getPositionFromList(Arrays.asList(LEVELS), party.getLevel()));

    }

    private void initCategory() {
        this.categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.findbuddies_spinner, getAllCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                party.setCategory(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setSelection(getPositionFromList(getAllCategories(), party.getCategory()));
    }

    private int getPositionFromList(List<String> list, String item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(item))
                return i;
        }
        return 0;
    }

    public abstract String getCategory();


    public void saveActivitySelected() {
        loadingProgressDialog.show();
        submit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.party_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.SAVE_ACTIVITY) {
            saveActivitySelected();
            return true;
        }
        else {
            finish();
            return true;
        }
    }

    void setTextOnNumberOfParticipantsButton(int numberOfParticipants) {
        this.numberOfParticipantsButton.setText("Desired participants : " + numberOfParticipants);
    }


    @Override
    public void update(Observable observable, Object o) {
        LocationInfo locationInfo = (LocationInfo) o;
        this.partyLocationButton.setText(locationInfo.getLocationStringForParty());
        Point location = new Point();
        location.setLat(locationInfo.getLatitude());
        location.setLon(locationInfo.getLongitude());
        this.party.setLocation(location);
        locationService.deactivate();
    }


    protected List<String> getAllCategories() {
        return CategoryType.names();
    }

    @Override
    public void updateLocationPoint(Point point, String address) {
        updateLocationAddress(address);
        this.party.setLocation(point);
    }

    @Override
    public void updateLocationAddress(String address) {
        partyLocationButton.setText(address);
    }



    @Override
    public void successOnPartyUpdate(String partyId) {
        dismissProgressDialog();
        goToTop(PartyActivity.this);
    }

    @Override
    public void errorOnPartyUpdate() {
        dismissProgressDialog();
        Toast.makeText(this, getString(R.string.party_save_error), Toast.LENGTH_LONG).show();
    }

    private void submit() {
        new UpdatePartyAsync(this, partyPictureBitmap).execute(party);
    }

    @Override
    public void errorOnGetPicture() {
        Toast.makeText(this, getText(R.string.picture_get_error), Toast.LENGTH_LONG).show();
        showProgressBar();
    }

    @Override
    public void successOnGetPicture(Bitmap bitmap, String pictureUrl) {
        this.partyPicture.setImageBitmap(bitmap);
        party.setPictureUrl(pictureUrl);
        this.partyPictureBitmap = null;
        showPicture();
    }

    private void showPicture() {
        if (this.viewSwitcher.getCurrentView() != this.partyPicture) {
            this.viewSwitcher.showNext();
        }
    }

    private void showProgressBar() {
        if (this.viewSwitcher.getCurrentView() == this.partyPicture) {
            this.viewSwitcher.showNext();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK)
            try {
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inDensity = 100;
                Bitmap bitmap = BitmapFactory.decodeStream(stream, null, opts);
                if (bitmap != null) {
                    if (partyPictureBitmap != null) {
                        partyPictureBitmap.recycle();
                    }
                    partyPictureBitmap = bitmap;
                    partyPicture.setImageBitmap(bitmap);
                    party.setPictureUrl(null);
                    showPicture();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
