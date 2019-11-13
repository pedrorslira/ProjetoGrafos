package com.grafos.service;



import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grafos.model.TipoDeUsuario;
import com.grafos.model.Usuario;
import com.grafos.repository.UsuarioRepository;

@Service
public class GrafoService {

	Graph<String, DefaultEdge> grafo = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	WebService webService;

	public Graph<String, DefaultEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<String, DefaultEdge> grafo) {
		this.grafo = grafo;
	}

	public Graph<String, DefaultEdge> preencherGrafo() {
		Usuario usuarioPrincipal = usuarioRepository.getPrincipal();
		grafo.addVertex(usuarioPrincipal.getNick());

		for (Usuario u : usuarioRepository.findAll()) {
			grafo.addVertex(u.getNick());
			if (u.getTipo() == TipoDeUsuario.SEGUIDOR) {
				grafo.addEdge(u.getNick(), u.getNickUsuarioSeguindo());
			}
		}
		return grafo;
	}

	public void dijkstra(String nick, String nick2) {
		DijkstraShortestPath <String,DefaultEdge> dijkstra = new DijkstraShortestPath<>(grafo);
		dijkstra.getPath(nick, nick2).getEdgeList().stream().forEach(System.out::println);
	}
}
