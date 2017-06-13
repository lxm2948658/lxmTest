package com.qianfan365.jcstore.common.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CouponExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andUnconditionalIsNull() {
            addCriterion("unconditional is null");
            return (Criteria) this;
        }

        public Criteria andUnconditionalIsNotNull() {
            addCriterion("unconditional is not null");
            return (Criteria) this;
        }

        public Criteria andUnconditionalEqualTo(Integer value) {
            addCriterion("unconditional =", value, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalNotEqualTo(Integer value) {
            addCriterion("unconditional <>", value, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalGreaterThan(Integer value) {
            addCriterion("unconditional >", value, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalGreaterThanOrEqualTo(Integer value) {
            addCriterion("unconditional >=", value, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalLessThan(Integer value) {
            addCriterion("unconditional <", value, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalLessThanOrEqualTo(Integer value) {
            addCriterion("unconditional <=", value, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalIn(List<Integer> values) {
            addCriterion("unconditional in", values, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalNotIn(List<Integer> values) {
            addCriterion("unconditional not in", values, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalBetween(Integer value1, Integer value2) {
            addCriterion("unconditional between", value1, value2, "unconditional");
            return (Criteria) this;
        }

        public Criteria andUnconditionalNotBetween(Integer value1, Integer value2) {
            addCriterion("unconditional not between", value1, value2, "unconditional");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("shop_id is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("shop_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Integer value) {
            addCriterion("shop_id =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Integer value) {
            addCriterion("shop_id <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Integer value) {
            addCriterion("shop_id >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("shop_id >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Integer value) {
            addCriterion("shop_id <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Integer value) {
            addCriterion("shop_id <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Integer> values) {
            addCriterion("shop_id in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Integer> values) {
            addCriterion("shop_id not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Integer value1, Integer value2) {
            addCriterion("shop_id between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Integer value1, Integer value2) {
            addCriterion("shop_id not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andStartonIsNull() {
            addCriterion("starton is null");
            return (Criteria) this;
        }

        public Criteria andStartonIsNotNull() {
            addCriterion("starton is not null");
            return (Criteria) this;
        }

        public Criteria andStartonEqualTo(Date value) {
            addCriterion("starton =", value, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonNotEqualTo(Date value) {
            addCriterion("starton <>", value, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonGreaterThan(Date value) {
            addCriterion("starton >", value, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonGreaterThanOrEqualTo(Date value) {
            addCriterion("starton >=", value, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonLessThan(Date value) {
            addCriterion("starton <", value, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonLessThanOrEqualTo(Date value) {
            addCriterion("starton <=", value, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonIn(List<Date> values) {
            addCriterion("starton in", values, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonNotIn(List<Date> values) {
            addCriterion("starton not in", values, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonBetween(Date value1, Date value2) {
            addCriterion("starton between", value1, value2, "starton");
            return (Criteria) this;
        }

        public Criteria andStartonNotBetween(Date value1, Date value2) {
            addCriterion("starton not between", value1, value2, "starton");
            return (Criteria) this;
        }

        public Criteria andEndonIsNull() {
            addCriterion("endon is null");
            return (Criteria) this;
        }

        public Criteria andEndonIsNotNull() {
            addCriterion("endon is not null");
            return (Criteria) this;
        }

        public Criteria andEndonEqualTo(Date value) {
            addCriterion("endon =", value, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonNotEqualTo(Date value) {
            addCriterion("endon <>", value, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonGreaterThan(Date value) {
            addCriterion("endon >", value, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonGreaterThanOrEqualTo(Date value) {
            addCriterion("endon >=", value, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonLessThan(Date value) {
            addCriterion("endon <", value, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonLessThanOrEqualTo(Date value) {
            addCriterion("endon <=", value, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonIn(List<Date> values) {
            addCriterion("endon in", values, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonNotIn(List<Date> values) {
            addCriterion("endon not in", values, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonBetween(Date value1, Date value2) {
            addCriterion("endon between", value1, value2, "endon");
            return (Criteria) this;
        }

        public Criteria andEndonNotBetween(Date value1, Date value2) {
            addCriterion("endon not between", value1, value2, "endon");
            return (Criteria) this;
        }

        public Criteria andMaximumIsNull() {
            addCriterion("maximum is null");
            return (Criteria) this;
        }

        public Criteria andMaximumIsNotNull() {
            addCriterion("maximum is not null");
            return (Criteria) this;
        }

        public Criteria andMaximumEqualTo(Integer value) {
            addCriterion("maximum =", value, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumNotEqualTo(Integer value) {
            addCriterion("maximum <>", value, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumGreaterThan(Integer value) {
            addCriterion("maximum >", value, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumGreaterThanOrEqualTo(Integer value) {
            addCriterion("maximum >=", value, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumLessThan(Integer value) {
            addCriterion("maximum <", value, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumLessThanOrEqualTo(Integer value) {
            addCriterion("maximum <=", value, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumIn(List<Integer> values) {
            addCriterion("maximum in", values, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumNotIn(List<Integer> values) {
            addCriterion("maximum not in", values, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumBetween(Integer value1, Integer value2) {
            addCriterion("maximum between", value1, value2, "maximum");
            return (Criteria) this;
        }

        public Criteria andMaximumNotBetween(Integer value1, Integer value2) {
            addCriterion("maximum not between", value1, value2, "maximum");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUsedNumberIsNull() {
            addCriterion("used_number is null");
            return (Criteria) this;
        }

        public Criteria andUsedNumberIsNotNull() {
            addCriterion("used_number is not null");
            return (Criteria) this;
        }

        public Criteria andUsedNumberEqualTo(Integer value) {
            addCriterion("used_number =", value, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberNotEqualTo(Integer value) {
            addCriterion("used_number <>", value, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberGreaterThan(Integer value) {
            addCriterion("used_number >", value, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("used_number >=", value, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberLessThan(Integer value) {
            addCriterion("used_number <", value, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberLessThanOrEqualTo(Integer value) {
            addCriterion("used_number <=", value, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberIn(List<Integer> values) {
            addCriterion("used_number in", values, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberNotIn(List<Integer> values) {
            addCriterion("used_number not in", values, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberBetween(Integer value1, Integer value2) {
            addCriterion("used_number between", value1, value2, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andUsedNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("used_number not between", value1, value2, "usedNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberIsNull() {
            addCriterion("receive_number is null");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberIsNotNull() {
            addCriterion("receive_number is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberEqualTo(Integer value) {
            addCriterion("receive_number =", value, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberNotEqualTo(Integer value) {
            addCriterion("receive_number <>", value, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberGreaterThan(Integer value) {
            addCriterion("receive_number >", value, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("receive_number >=", value, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberLessThan(Integer value) {
            addCriterion("receive_number <", value, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberLessThanOrEqualTo(Integer value) {
            addCriterion("receive_number <=", value, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberIn(List<Integer> values) {
            addCriterion("receive_number in", values, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberNotIn(List<Integer> values) {
            addCriterion("receive_number not in", values, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberBetween(Integer value1, Integer value2) {
            addCriterion("receive_number between", value1, value2, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andReceiveNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("receive_number not between", value1, value2, "receiveNumber");
            return (Criteria) this;
        }

        public Criteria andCouponIdIsNull() {
            addCriterion("coupon_id is null");
            return (Criteria) this;
        }

        public Criteria andCouponIdIsNotNull() {
            addCriterion("coupon_id is not null");
            return (Criteria) this;
        }

        public Criteria andCouponIdEqualTo(Integer value) {
            addCriterion("coupon_id =", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdNotEqualTo(Integer value) {
            addCriterion("coupon_id <>", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdGreaterThan(Integer value) {
            addCriterion("coupon_id >", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("coupon_id >=", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdLessThan(Integer value) {
            addCriterion("coupon_id <", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdLessThanOrEqualTo(Integer value) {
            addCriterion("coupon_id <=", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdIn(List<Integer> values) {
            addCriterion("coupon_id in", values, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdNotIn(List<Integer> values) {
            addCriterion("coupon_id not in", values, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdBetween(Integer value1, Integer value2) {
            addCriterion("coupon_id between", value1, value2, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdNotBetween(Integer value1, Integer value2) {
            addCriterion("coupon_id not between", value1, value2, "couponId");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andCirculationIsNull() {
            addCriterion("circulation is null");
            return (Criteria) this;
        }

        public Criteria andCirculationIsNotNull() {
            addCriterion("circulation is not null");
            return (Criteria) this;
        }

        public Criteria andCirculationEqualTo(Integer value) {
            addCriterion("circulation =", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationNotEqualTo(Integer value) {
            addCriterion("circulation <>", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationGreaterThan(Integer value) {
            addCriterion("circulation >", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationGreaterThanOrEqualTo(Integer value) {
            addCriterion("circulation >=", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationLessThan(Integer value) {
            addCriterion("circulation <", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationLessThanOrEqualTo(Integer value) {
            addCriterion("circulation <=", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationIn(List<Integer> values) {
            addCriterion("circulation in", values, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationNotIn(List<Integer> values) {
            addCriterion("circulation not in", values, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationBetween(Integer value1, Integer value2) {
            addCriterion("circulation between", value1, value2, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationNotBetween(Integer value1, Integer value2) {
            addCriterion("circulation not between", value1, value2, "circulation");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(Double value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(Double value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(Double value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(Double value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(Double value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<Double> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<Double> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(Double value1, Double value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(Double value1, Double value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyIsNull() {
            addCriterion("required_money is null");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyIsNotNull() {
            addCriterion("required_money is not null");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyEqualTo(Double value) {
            addCriterion("required_money =", value, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyNotEqualTo(Double value) {
            addCriterion("required_money <>", value, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyGreaterThan(Double value) {
            addCriterion("required_money >", value, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("required_money >=", value, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyLessThan(Double value) {
            addCriterion("required_money <", value, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyLessThanOrEqualTo(Double value) {
            addCriterion("required_money <=", value, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyIn(List<Double> values) {
            addCriterion("required_money in", values, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyNotIn(List<Double> values) {
            addCriterion("required_money not in", values, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyBetween(Double value1, Double value2) {
            addCriterion("required_money between", value1, value2, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andRequiredMoneyNotBetween(Double value1, Double value2) {
            addCriterion("required_money not between", value1, value2, "requiredMoney");
            return (Criteria) this;
        }

        public Criteria andShareableIsNull() {
            addCriterion("shareable is null");
            return (Criteria) this;
        }

        public Criteria andShareableIsNotNull() {
            addCriterion("shareable is not null");
            return (Criteria) this;
        }

        public Criteria andShareableEqualTo(Integer value) {
            addCriterion("shareable =", value, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableNotEqualTo(Integer value) {
            addCriterion("shareable <>", value, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableGreaterThan(Integer value) {
            addCriterion("shareable >", value, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableGreaterThanOrEqualTo(Integer value) {
            addCriterion("shareable >=", value, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableLessThan(Integer value) {
            addCriterion("shareable <", value, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableLessThanOrEqualTo(Integer value) {
            addCriterion("shareable <=", value, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableIn(List<Integer> values) {
            addCriterion("shareable in", values, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableNotIn(List<Integer> values) {
            addCriterion("shareable not in", values, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableBetween(Integer value1, Integer value2) {
            addCriterion("shareable between", value1, value2, "shareable");
            return (Criteria) this;
        }

        public Criteria andShareableNotBetween(Integer value1, Integer value2) {
            addCriterion("shareable not between", value1, value2, "shareable");
            return (Criteria) this;
        }

        public Criteria andSerialIsNull() {
            addCriterion("serial is null");
            return (Criteria) this;
        }

        public Criteria andSerialIsNotNull() {
            addCriterion("serial is not null");
            return (Criteria) this;
        }

        public Criteria andSerialEqualTo(String value) {
            addCriterion("serial =", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotEqualTo(String value) {
            addCriterion("serial <>", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialGreaterThan(String value) {
            addCriterion("serial >", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialGreaterThanOrEqualTo(String value) {
            addCriterion("serial >=", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialLessThan(String value) {
            addCriterion("serial <", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialLessThanOrEqualTo(String value) {
            addCriterion("serial <=", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialLike(String value) {
            addCriterion("serial like", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotLike(String value) {
            addCriterion("serial not like", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialIn(List<String> values) {
            addCriterion("serial in", values, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotIn(List<String> values) {
            addCriterion("serial not in", values, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialBetween(String value1, String value2) {
            addCriterion("serial between", value1, value2, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotBetween(String value1, String value2) {
            addCriterion("serial not between", value1, value2, "serial");
            return (Criteria) this;
        }

        public Criteria andUsedIsNull() {
            addCriterion("used is null");
            return (Criteria) this;
        }

        public Criteria andUsedIsNotNull() {
            addCriterion("used is not null");
            return (Criteria) this;
        }

        public Criteria andUsedEqualTo(Integer value) {
            addCriterion("used =", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedNotEqualTo(Integer value) {
            addCriterion("used <>", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedGreaterThan(Integer value) {
            addCriterion("used >", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedGreaterThanOrEqualTo(Integer value) {
            addCriterion("used >=", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedLessThan(Integer value) {
            addCriterion("used <", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedLessThanOrEqualTo(Integer value) {
            addCriterion("used <=", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedIn(List<Integer> values) {
            addCriterion("used in", values, "used");
            return (Criteria) this;
        }

        public Criteria andUsedNotIn(List<Integer> values) {
            addCriterion("used not in", values, "used");
            return (Criteria) this;
        }

        public Criteria andUsedBetween(Integer value1, Integer value2) {
            addCriterion("used between", value1, value2, "used");
            return (Criteria) this;
        }

        public Criteria andUsedNotBetween(Integer value1, Integer value2) {
            addCriterion("used not between", value1, value2, "used");
            return (Criteria) this;
        }

        public Criteria andDeleteIsNull() {
            addCriterion("delete is null");
            return (Criteria) this;
        }

        public Criteria andDeleteIsNotNull() {
            addCriterion("delete is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteEqualTo(Integer value) {
            addCriterion("delete =", value, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteNotEqualTo(Integer value) {
            addCriterion("delete <>", value, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteGreaterThan(Integer value) {
            addCriterion("delete >", value, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("delete >=", value, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteLessThan(Integer value) {
            addCriterion("delete <", value, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("delete <=", value, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteIn(List<Integer> values) {
            addCriterion("delete in", values, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteNotIn(List<Integer> values) {
            addCriterion("delete not in", values, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteBetween(Integer value1, Integer value2) {
            addCriterion("delete between", value1, value2, "delete");
            return (Criteria) this;
        }

        public Criteria andDeleteNotBetween(Integer value1, Integer value2) {
            addCriterion("delete not between", value1, value2, "delete");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}