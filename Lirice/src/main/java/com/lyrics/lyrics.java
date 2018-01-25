package com.lyrics;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class lyrics {
	
	
	
@GetMapping(value = "/hellow")
@Transactional
  public String test() {
	  return  "Hello";
  }



  

}
