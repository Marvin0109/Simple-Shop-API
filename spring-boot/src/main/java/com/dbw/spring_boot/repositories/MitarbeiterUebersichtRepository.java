package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.MitarbeiterBestellstatusDTO;
import com.dbw.spring_boot.model.MitarbeiterUebersichtDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MitarbeiterUebersichtRepository {

    private final JdbcTemplate jdbcTemplate;

    public MitarbeiterUebersichtRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MitarbeiterUebersichtDTO> findAll() {
        String sql = "SELECT * FROM v_mitarbeiter_uebersicht";
        return jdbcTemplate.query(sql, rowMapperMU());
    }

    public List<MitarbeiterBestellstatusDTO> findAllBestellStatus() {
        String sql ="SELECT * FROM v_mitarbeiter_bestellstatus_uebersicht";
        return jdbcTemplate.query(sql, rowMapperBS());
    }

    private RowMapper<MitarbeiterUebersichtDTO> rowMapperMU() {
        return (rs, rowNum) -> new MitarbeiterUebersichtDTO(
            rs.getInt("personal_nr"),
            rs.getInt("anzahl_verwalteter_bestellungen"),
            rs.getInt("anzahl_angelegter_produkte")
        );
    }

    private RowMapper<MitarbeiterBestellstatusDTO> rowMapperBS() {
        return (rs, rowNum) -> new MitarbeiterBestellstatusDTO(
                rs.getInt("personal_nr"),
                rs.getString("status"),
                rs.getInt("anzahl_bestellungen")
        );
    }

}
