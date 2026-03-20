package com.trade.margin.repository;

import com.trade.margin.dto.MarginCallTargetDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MarginCallMapper {

    List<MarginCallTargetDto> selectMarginCallTargets(@Param("limit") int limit);
}
