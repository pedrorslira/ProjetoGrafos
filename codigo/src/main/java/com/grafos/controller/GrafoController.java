package com.grafos.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grafos.service.GrafoService;
import com.grafos.view.GrafoView;

@RestController
@CrossOrigin
@RequestMapping("/grafo")
public class GrafoController {

	@Autowired
	GrafoService grafoService;
	@Autowired
	GrafoView grafoView;
	
	@PostMapping("/preencher")
	public void preencherGrafo() {
		grafoService.preencherGrafo();
	}
	
	@GetMapping("/exibir")
	public void exibirGrafo() throws IOException {
		grafoView.exibirGrafo();
	}
	
	@GetMapping("/menorCaminho/{nick}/{nick2}")
	public void dijkstra(@PathVariable String nick, @PathVariable String nick2) {
		grafoService.dijkstra(nick, nick2);
	}
	
}
