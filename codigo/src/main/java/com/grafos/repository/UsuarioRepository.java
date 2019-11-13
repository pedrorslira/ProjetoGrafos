package com.grafos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grafos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = "select * from tb_usuario where tipo = '0'",nativeQuery = true)
	public List<Usuario> getSeguidores();
	
	@Query(value = "select * from tb_usuario where tipo = '1'",nativeQuery = true)
	public List<Usuario> getSeguindo();
	
	@Query(value = "select * from tb_usuario where tipo = '2'",nativeQuery = true)
	public Usuario getPrincipal();
	
	@Query(value = "select * from tb_usuario where id_twitter like :idTwitter",nativeQuery = true)
	public List<Usuario> getUsuarioByIdTwitter(@Param("idTwitter")long idTwitter);
	
	@Query(value = "select * from tb_usuario where nick like :nick",nativeQuery = true)
	public Usuario getUsuarioByNick(@Param("nick")String nick);
	
	@Query(value = "select count(*) from tb_usuario where id_twitter like :idTwitter",nativeQuery = true)
	public int getQtdUsuario(@Param("idTwitter")long idTwitter);
}
