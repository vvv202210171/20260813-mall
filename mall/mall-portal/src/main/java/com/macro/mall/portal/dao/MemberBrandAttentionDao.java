package com.macro.mall.portal.dao;

import com.macro.mall.portal.domain.MemberBrandAttention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class MemberBrandAttentionDao {
    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public MemberBrandAttentionDao(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(MemberBrandAttention item) {
        if (item.getId() == null) item.setId(UUID.randomUUID().toString());
        String sql = "INSERT INTO member_brand_attention (id, member_id, member_nickname, member_icon, brand_id, brand_name, brand_logo, brand_city, create_time) " +
                "VALUES (:id, :memberId, :nickname, :icon, :brandId, :brandName, :brandLogo, :brandCity, :createTime)";
        Map<String, Object> params = Map.of(
                "id", item.getId(),
                "memberId", item.getMemberId(),
                "nickname", item.getMemberNickname(),
                "icon", item.getMemberIcon(),
                "brandId", item.getBrandId(),
                "brandName", item.getBrandName(),
                "brandLogo", item.getBrandLogo(),
                "brandCity", item.getBrandCity(),
                "createTime", item.getCreateTime()
        );
        jdbc.update(sql, params);
    }

    public int deleteByMemberIdAndBrandId(Long memberId, Long brandId) {
        String sql = "DELETE FROM member_brand_attention WHERE member_id = :memberId AND brand_id = :brandId";
        return jdbc.update(sql, Map.of("memberId", memberId, "brandId", brandId));
    }

    public List<MemberBrandAttention> findByMemberId(Long memberId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        String sql = "SELECT * FROM member_brand_attention WHERE member_id = :memberId ORDER BY create_time DESC LIMIT :offset, :limit";
        List<MemberBrandAttention> list = jdbc.query(sql, Map.of("memberId", memberId, "offset", offset, "limit", pageSize), BeanPropertyRowMapper.newInstance(MemberBrandAttention.class));
        return list;
    }

    public MemberBrandAttention findByMemberIdAndBrandId(Long memberId, Long brandId) {
        String sql = "SELECT * FROM member_brand_attention WHERE member_id = :memberId AND brand_id = :brandId LIMIT 1";
        List<MemberBrandAttention> list = jdbc.query(sql, Map.of("memberId", memberId, "brandId", brandId), BeanPropertyRowMapper.newInstance(MemberBrandAttention.class));
        return list.isEmpty() ? null : list.get(0);
    }

    public void deleteAllByMemberId(Long memberId) {
        jdbc.update("DELETE FROM member_brand_attention WHERE member_id = :memberId", Map.of("memberId", memberId));
    }
}
