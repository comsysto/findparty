package org.comsysto.labs.web;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.comsysto.labs.user.User;
import org.comsysto.labs.util.URLEncodeDecodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Controller
@RequestMapping("/user")
public class UserService {

    public static final String USER = "user";
    private final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    @Qualifier("userMongoOperations")
    public MongoOperations userMongoOperations;

	@RequestMapping(value = "/new/{name}/{age}/{email}", method = RequestMethod.PUT)
	@ResponseStatus( HttpStatus.OK )
	public void add(@PathVariable("name") String name,
                    @PathVariable("age") Integer age,
                    @PathVariable("email") String email) throws Exception {
        userMongoOperations.insert(new User(URLEncodeDecodeUtil.decodeString(name), age, URLEncodeDecodeUtil.decodeString(email)));
        LOG.info(userMongoOperations.findOne(new Query(where("email").is(URLEncodeDecodeUtil.decodeString(email))),
                User.class));
	}

	@RequestMapping(value = "/drop/{email}", method = RequestMethod.DELETE)
	@ResponseStatus( HttpStatus.OK )
	public void drop(@PathVariable("email") String email)
			throws Exception {
		User perosonToDelete = userMongoOperations.findOne(
				new Query(where("email").is(URLEncodeDecodeUtil.decodeString(email))), User.class);
		userMongoOperations.remove(
                new Query(where("email").is(perosonToDelete.getEmail())),
                User.class);
		LOG.info(userMongoOperations.findOne(
                new Query(where("id").is(perosonToDelete.getId())),
                User.class));
	}

	@RequestMapping(value = "/get/{email}", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
	public List<User> get(@PathVariable("email") String email) throws Exception {
		List<User> result = new ArrayList<User>();
		if (StringUtils.isEmpty(email)) {
			result.addAll(userMongoOperations.findAll(User.class));
		} else {
			result.add(userMongoOperations.findOne(
                    new Query(where("email").is(URLDecoder.decode(email, "UTF-8"))), User.class));
		}
		return result;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
	public List<User> getAll() throws Exception {
		return get(null);
	}
	

}
