package com.example.demo1.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository public class TDemoRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true) public List<TDemo> findAll() {
        return jdbcTemplate.query("select * from t_demo", new TDemoMapper());
    }

    @Transactional(readOnly = true) public TDemo findTDemoById(String id) {
        return jdbcTemplate.queryForObject("select * from t_demo where c_id=?", new Object[] {id}, new TDemoMapper());
    }

    public int create(final TDemo demo) {
        final String sql = "insert into t_demo(c_id,n_value) values(?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, demo.getId());
            ps.setInt(2, demo.getValue());
            return ps;
        }, holder);
        return holder.getKey().intValue();
    }

    public void update(final TDemo demo) {
        jdbcTemplate.update("update t_demo set n_value=? where c_id=?", new Object[] {demo.getValue(), demo.getId()});
    }

    /**
     * 编码自增长
     * @return
     */
    public synchronized int generatorBm(){
        int value = 1;
        TDemo demo = this.findTDemoById("demo");
        if(demo == null){
            demo.setValue(value);
            demo.setId("demo");
            this.create(demo);
        }else{
            value = demo.getValue() +1;
            demo.setValue(value);
            demo.setId("demo");
            this.update(demo);
        }
        return value;
    }
}

class TDemoMapper implements RowMapper<TDemo> {

    @Override public TDemo mapRow(ResultSet rs, int rowNum) throws SQLException {
        TDemo demo = new TDemo();
        demo.setId(rs.getString("c_id"));
        demo.setValue(rs.getInt("n_value"));
        return demo;
    }

}