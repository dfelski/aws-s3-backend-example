package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MyController {

	private final StorageService storageService;

	@Autowired
	public MyController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String list(Model model) {
		model.addAttribute("files", storageService.loadAll());
		return "index";
	}

	@PostMapping("/")
	public String handleUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
		int statusCode = storageService.store(file);
		String message;
		if(statusCode == HttpStatus.OK.value()) {
			message = file.getOriginalFilename() + " successfully uploaded !";
		} else {
			message = "something went wrong :(";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/";
	}
}
