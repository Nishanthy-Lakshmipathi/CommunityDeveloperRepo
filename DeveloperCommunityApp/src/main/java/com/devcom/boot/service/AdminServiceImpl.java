package com.devcom.boot.service;

import java.net.PasswordAuthentication;			
import java.util.List;	
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import com.devcom.boot.entity.Admin;
import com.devcom.boot.entity.Developer;
import com.devcom.boot.entity.Feed;
import com.devcom.boot.entity.User;
import com.devcom.boot.exception.AdminNotFoundException;
import com.devcom.boot.exception.FeedNotFoundException;
import com.devcom.boot.exception.UserNotFoundException;
import com.devcom.boot.repository.AdminRepository;
import com.devcom.boot.repository.DeveloperRepository;
import com.devcom.boot.repository.FeedRepository;
import com.devcom.boot.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminServiceInterface{
	
	@Autowired AdminRepository adminRepo;
	@Autowired DeveloperRepository devRepo;
	@Autowired FeedRepository feedRepo;
	@Autowired UserRepository userRepo;
	@Autowired JavaMailSender emailSender;
	
	public List<Admin> getAllAdmins() {
		List<Admin> listOfAdmins = (List<Admin>) adminRepo.findAll();
		return listOfAdmins;
	}
	public Optional<Admin> getAdminById(Integer adminId) {
		
		Optional<Admin> admin = adminRepo.findById(adminId);
		if(!admin.isPresent())
			throw new AdminNotFoundException("Admin With Id "+ adminId+" Not Found");
		return admin;
	}
	
	public Optional<Developer> getDeveloperById(Integer devId) {
		Optional<Developer> developer = devRepo.findById(devId);
		
//		  if(!developer.isPresent()) throw new
//		  DeveloperNotFoundException("Developer With Id "+ devId+" Not Found");
		 
		return developer;
	}
	
	
//	@Override
//	public Developer validateDeveloperById(Integer devId) {
//		Developer exists = devRepo.findByDevId(devId);
//		 if(exists == null)
//		throw  new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
//	boolean value = exists.getIsVerified();
//	
//		 if(!value) {
//			  
//			  exists.setIsVerified(true);
//		 	  devRepo.save(exists);
//		 	  
//		 }
//		 else 
//			 throw new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
//			 
//		return exists;
//			
//	}
	
	@Override
	public Optional<User> saveValidUser(User user,Integer devId) {
		Optional<User> existing = userRepo.findById(user.getUserId());
		Developer exists = devRepo.findByDevId(devId);
		
		 if(exists == null)
				throw  new AdminNotFoundException("Developer With Id "+ devId+" Not Found");
			boolean value = exists.getIsVerified();
			
			if(!value) {
				  
				  exists.setIsVerified(true);
			 	  devRepo.save(exists);
			 	 if(!existing.isPresent()) {
						userRepo.save(user);
						return existing;
					}
					throw new UserNotFoundException("User With Id "+ user.getUserId() +" exists already");
			 	  
			 }
			 else 
				 throw new AdminNotFoundException("blabla With Id "+ devId+" Not Found");	
	}
	@Override
	public Developer invalidateDeveloperById(Integer devId) {
		Developer exists = devRepo.findByDevId(devId);
		 if(exists == null)
		throw  new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
	boolean value = exists.getIsVerified();
	
		 if(value) {
			  
			  exists.setIsVerified(false);
		 	  devRepo.save(exists);
		 }
		 else 
			 throw new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
		return exists;
				
	}
	
	@Override
	public Developer updateStatusDeveloper(Integer devId, String choice) {
		Developer exists = devRepo.findByDevId(devId);
		 if(exists == null)
		throw  new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
	boolean value = exists.getIsBlocked();
	
		switch(choice) {
		case "block": if(value) {
			  
			  			exists.setIsBlocked(false);
						 	  devRepo.save(exists);
						 }
						 else 
							 throw new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
						return exists;
		
		case "unblock":	if(!value) {
			  
						  exists.setIsBlocked(true);
					 	  devRepo.save(exists);
					 }
					 else 
						 throw new AdminNotFoundException("Devloper With Id "+ devId+" Not Found");
					return exists;
					
		default: throw new AdminNotFoundException("block/unblock");
		}
		
	}
	@Override
	public Optional<Feed> deleteFeedAdmin(Integer feedId) {
		Optional<Feed> existing = feedRepo.findById(feedId);
		
		if(existing.isPresent()) {
			feedRepo.deleteById(feedId);
			return existing;
		}
		else
			throw new FeedNotFoundException("Feed With Id "+ feedId +" does not exist");
		
	}
	@Override
	public void sendSimpleEmail(String email, String string, String string2) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		  simpleMailMessage.setTo(email);
		  simpleMailMessage.setSubject("checkmail");
		  simpleMailMessage.setText("Check if this mail is recieved");
		  simpleMailMessage.setFrom("nishanthylakshmipathy72@gmail.com");
		  
		  
		  emailSender.send(simpleMailMessage);
	}
	@Override
	public List<Feed> getAllFeeds() {
		
		List<Feed> listOfFeeds = (List<Feed>) feedRepo.findAll();
		return listOfFeeds;
	}
	
	
		
}
