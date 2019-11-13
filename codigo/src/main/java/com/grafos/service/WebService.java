package com.grafos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grafos.model.TipoDeUsuario;
import com.grafos.model.Usuario;
import com.grafos.repository.UsuarioRepository;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class WebService {

	@Autowired
	UsuarioRepository usuarioRepository;

	static long cursor = -1;
	static IDs idsSeguidores, idsSeguindo;
	static User user;
	static Usuario usuario;
	static final long SEGUNDO = 1000;
	static final long MINUTO = SEGUNDO * 60;

	public void coletaDeDados(String nickname, String consumerKey, String consumerSecret, String acessToken,
			String acessSecret) throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(acessToken).setOAuthAccessTokenSecret(acessSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		user = twitter.showUser(nickname);
		usuario = new Usuario();
		usuario.setIdTwitter(user.getId());
		usuario.setNome(user.getName());
		usuario.setNick(user.getScreenName());
		usuario.setTipo(TipoDeUsuario.PRINCIPAL);
		String nickUsuarioPrincipal = usuario.getNick(); 
		usuarioRepository.save(usuario);
		do {
			idsSeguidores = twitter.getFollowersIDs(nickname, cursor);
			for (long id : idsSeguidores.getIDs()) {
				user = twitter.showUser(id);
				novoUsuario(id, nickUsuarioPrincipal);
			}
		} while ((cursor = idsSeguidores.getNextCursor()) != 0);
		seguidoresDeSeguidores(consumerKey, consumerSecret, acessToken, acessSecret);
	}

	public void seguidoresDeSeguidores(String consumerKey, String consumerSecret, String acessToken, String acessSecret)
			throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(acessToken).setOAuthAccessTokenSecret(acessSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		cursor = -1;

		List<Usuario> seguidores = usuarioRepository.getSeguidores();
		for (Usuario u : seguidores) {
				idsSeguidores = twitter.getFollowersIDs(u.getNick(), cursor);
				for (long id : idsSeguidores.getIDs()) {
					user = twitter.showUser(id);
					novoUsuario(id, u.getNick());
				}
		}
	}

	public void novoUsuario(long id, String nickUsuarioSeguindo) {
		usuario = new Usuario();
		usuario.setIdTwitter(id);
		usuario.setNome(user.getName());
		usuario.setNick(user.getScreenName());
		usuario.setTipo(TipoDeUsuario.SEGUIDOR);
		usuario.setNickUsuarioSeguindo(nickUsuarioSeguindo);
		usuarioRepository.save(usuario);
		if (usuario.getId() % 800 == 0) {
			try {
				Thread.sleep(MINUTO * 15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
