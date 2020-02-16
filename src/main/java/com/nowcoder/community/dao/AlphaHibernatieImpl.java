package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")
@Primary
public class AlphaHibernatieImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernatie";
    }
}
