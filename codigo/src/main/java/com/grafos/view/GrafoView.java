package com.grafos.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.grafos.service.GrafoService;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.util.mxCellRenderer;

@Component
public class GrafoView {

	@Autowired
	GrafoService grafoService;

	public void exibirGrafo() throws IOException {
		File imagem = new File("src/main/grafo.png");
		imagem.createNewFile();
		
		JGraphXAdapter<String, DefaultEdge> grafo = new JGraphXAdapter<String, DefaultEdge>(grafoService.getGrafo());	
		mxFastOrganicLayout layout = new mxFastOrganicLayout(grafo);
		layout.setForceConstant(250); 
		layout.setDisableEdgeStyle(false);
		layout.execute(grafo.getDefaultParent());
		BufferedImage image = mxCellRenderer.createBufferedImage(grafo, null, 2, Color.WHITE, true, null);
		File imgFile = new File("src/main/grafo.png");
		ImageIO.write(image, "PNG", imgFile);
	}
}
