package com.macro.mall.portal.dao;

import com.macro.mall.portal.domain.MemberProductCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class MemberProductCollectionDao {
    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public MemberProductCollectionDao(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(MemberProductCollection item) {
        if (item.getId() == null) item.setId(UUID.randomUUID().toString());
        String sql = "INSERT INTO member_product_collection (id, member_id, member_nickname, member_icon, product_id, product_name, product_pic, product_sub_title, product_price, create_time) " +
                "VALUES (:id, :memberId, :nickname, :icon, :productId, :productName, :productPic, :productSubTitle, :productPrice, :createTime)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", item.getId());
        params.put("memberId", item.getMemberId());
        params.put("nickname", item.getMemberNickname());
        params.put("icon", item.getMemberIcon());
        params.put("productId", item.getProductId());
        params.put("productName", item.getProductName());
        params.put("productPic", item.getProductPic());
        params.put("productSubTitle", item.getProductSubTitle());
        params.put("productPrice", item.getProductPrice());
        params.put("createTime", item.getCreateTime());
        jdbc.update(sql, new MapSqlParameterSource(params));
    }

    public int deleteByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "DELETE FROM member_product_collection WHERE member_id = :memberId AND product_id = :productId";
        Map<String, Object> params = Map.of("memberId", memberId, "productId", productId);
        return jdbc.update(sql, params);
    }

    public MemberProductCollection findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT * FROM member_product_collection WHERE member_id = :memberId AND product_id = :productId LIMIT 1";
        Map<String, Object> params = Map.of("memberId", memberId, "productId", productId);
        List<MemberProductCollection> list = jdbc.query(sql, params, BeanPropertyRowMapper.newInstance(MemberProductCollection.class));
        return list.isEmpty() ? null : list.get(0);
    }

    public Page<MemberProductCollection> findByMemberId(Long memberId, int pageNum, int pageSize) {
        String countSql = "SELECT COUNT(1) FROM member_product_collection WHERE member_id = :memberId";
        Integer total = jdbc.queryForObject(countSql, Map.of("memberId", memberId), Integer.class);
        int offset = (pageNum - 1) * pageSize;
        String sql = "SELECT * FROM member_product_collection WHERE member_id = :memberId ORDER BY create_time DESC LIMIT :offset, :limit";
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("offset", offset);
        params.put("limit", pageSize);
        List<MemberProductCollection> list = jdbc.query(sql, params, BeanPropertyRowMapper.newInstance(MemberProductCollection.class));
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return new PageImpl<>(list, pageable, total == null ? 0 : total);
    }

    public void deleteAllByMemberId(Long memberId) {
        String sql = "DELETE FROM member_product_collection WHERE member_id = :memberId";
        jdbc.update(sql, Map.of("memberId", memberId));
    }
}
