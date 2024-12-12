package com.example.config;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.model.Role;
import com.example.model.Word;
import com.example.repository.RoleRepository;
import com.example.repository.WordRepository;
import com.example.service.RoleService;
import com.example.service.WordService;

@Component
public class InitWords implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InitWords.class);
    
	private WordService wordService;
	private WordRepository wordRepository;
	private RoleService roleService;
	private RoleRepository roleRepository;

	@Autowired
	public InitWords(WordService wordService, WordRepository wordRepository,RoleService roleService,RoleRepository roleRepository) {
		logger.info("InitWords component initialized");
		this.wordService = wordService;
		this.wordRepository = wordRepository;
		this.roleService = roleService;
		this.roleRepository = roleRepository;

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
			List<Role> roles = roleRepository.findAll();
            logger.info("Roles fetched from DB: {}", roles);
			if(roles.isEmpty()) {
				logger.info("Fetching...");
				roleService.initRoles();
			}
			List<Word> all = wordRepository.findAll();
			if (all.isEmpty()) {
				try {
					wordService.initWords();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


	}

}
