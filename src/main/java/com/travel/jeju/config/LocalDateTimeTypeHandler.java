package com.travel.jeju.config;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.travel.jeju.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {

        Instant param = DateUtil.localDateTimeToInstant(parameter);

        ps.setObject(i, param);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        
        LocalDateTime result = null;

        try {
            Instant instant = rs.getTimestamp(columnName).toInstant();
            result = DateUtil.instantToLocalDateTime(instant, "Asia/Seoul");
        } catch (Exception e) {

        }


        return result;
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Instant instant = rs.getObject(columnIndex, Instant.class);

        LocalDateTime result = null;

        try {
            result = DateUtil.instantToLocalDateTime(instant, "Asia/Seoul");
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return result;
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Instant instant = cs.getObject(columnIndex, Instant.class);

        LocalDateTime result = null;

        try {
            result = DateUtil.instantToLocalDateTime(instant, "Asia/Seoul");
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

}
