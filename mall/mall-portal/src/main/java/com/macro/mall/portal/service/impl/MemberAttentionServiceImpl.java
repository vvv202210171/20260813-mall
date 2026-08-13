package com.macro.mall.portal.service.impl;

import com.macro.mall.mapper.PmsBrandMapper;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.domain.MemberBrandAttention;
import com.macro.mall.portal.dao.MemberBrandAttentionDao;
import com.macro.mall.portal.service.MemberAttentionService;
import com.macro.mall.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 会员关注Service实现类
 * Created by macro on 2018/8/2.
 */
@Service
public class MemberAttentionServiceImpl implements MemberAttentionService {
    @Value("${mongo.insert.sqlEnable}")
    private Boolean sqlEnable;
    @Autowired
    private PmsBrandMapper brandMapper;
    @Autowired
    private MemberBrandAttentionDao memberBrandAttentionDao;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public int add(MemberBrandAttention memberBrandAttention) {
        int count = 0;
        if(memberBrandAttention.getBrandId()==null){
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttention.setMemberId(member.getId());
        memberBrandAttention.setMemberNickname(member.getNickname());
        memberBrandAttention.setMemberIcon(member.getIcon());
        memberBrandAttention.setCreateTime(new Date());
        MemberBrandAttention findAttention = memberBrandAttentionDao.findByMemberIdAndBrandId(memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId());
        if (findAttention == null) {
            if(sqlEnable){
                PmsBrand brand = brandMapper.selectByPrimaryKey(memberBrandAttention.getBrandId());
                if(brand==null){
                    return 0;
                }else{
                    memberBrandAttention.setBrandCity(null);
                    memberBrandAttention.setBrandName(brand.getName());
                    memberBrandAttention.setBrandLogo(brand.getLogo());
                }
            }
            memberBrandAttentionDao.save(memberBrandAttention);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionDao.deleteByMemberIdAndBrandId(member.getId(),brandId);
    }

    @Override
    public Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        // memberBrandAttentionDao returns a list; wrap into PageImpl for compatibility
        List<com.macro.mall.portal.domain.MemberBrandAttention> list = memberBrandAttentionDao.findByMemberId(member.getId(), pageNum, pageSize);
        int total = list.size();
        return new org.springframework.data.domain.PageImpl<>(list, PageRequest.of(pageNum-1,pageSize), total);
    }

    @Override
    public MemberBrandAttention detail(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionDao.findByMemberIdAndBrandId(member.getId(), brandId);
    }

    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttentionDao.deleteAllByMemberId(member.getId());
    }
}
