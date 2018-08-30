package com.targaryen.octopus.dao;

import com.targaryen.octopus.vo.NoteVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  @author Liu Mengyang
 * */
@Repository
public interface NoteRepository extends JpaRepository<NoteVo, Long> {

}