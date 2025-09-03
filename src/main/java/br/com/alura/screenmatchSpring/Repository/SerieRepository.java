package br.com.alura.screenmatchSpring.Repository;

import br.com.alura.screenmatchSpring.Model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
