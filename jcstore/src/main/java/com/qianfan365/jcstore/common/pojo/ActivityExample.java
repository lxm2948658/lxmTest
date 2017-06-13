package com.qianfan365.jcstore.common.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ActivityExample() {
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

        public Criteria andEverydayTimesIsNull() {
            addCriterion("everyday_times is null");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesIsNotNull() {
            addCriterion("everyday_times is not null");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesEqualTo(Integer value) {
            addCriterion("everyday_times =", value, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesNotEqualTo(Integer value) {
            addCriterion("everyday_times <>", value, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesGreaterThan(Integer value) {
            addCriterion("everyday_times >", value, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("everyday_times >=", value, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesLessThan(Integer value) {
            addCriterion("everyday_times <", value, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesLessThanOrEqualTo(Integer value) {
            addCriterion("everyday_times <=", value, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesIn(List<Integer> values) {
            addCriterion("everyday_times in", values, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesNotIn(List<Integer> values) {
            addCriterion("everyday_times not in", values, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesBetween(Integer value1, Integer value2) {
            addCriterion("everyday_times between", value1, value2, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andEverydayTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("everyday_times not between", value1, value2, "everydayTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesIsNull() {
            addCriterion("total_times is null");
            return (Criteria) this;
        }

        public Criteria andTotalTimesIsNotNull() {
            addCriterion("total_times is not null");
            return (Criteria) this;
        }

        public Criteria andTotalTimesEqualTo(Integer value) {
            addCriterion("total_times =", value, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesNotEqualTo(Integer value) {
            addCriterion("total_times <>", value, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesGreaterThan(Integer value) {
            addCriterion("total_times >", value, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_times >=", value, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesLessThan(Integer value) {
            addCriterion("total_times <", value, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesLessThanOrEqualTo(Integer value) {
            addCriterion("total_times <=", value, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesIn(List<Integer> values) {
            addCriterion("total_times in", values, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesNotIn(List<Integer> values) {
            addCriterion("total_times not in", values, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesBetween(Integer value1, Integer value2) {
            addCriterion("total_times between", value1, value2, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andTotalTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("total_times not between", value1, value2, "totalTimes");
            return (Criteria) this;
        }

        public Criteria andProbalilityIsNull() {
            addCriterion("probalility is null");
            return (Criteria) this;
        }

        public Criteria andProbalilityIsNotNull() {
            addCriterion("probalility is not null");
            return (Criteria) this;
        }

        public Criteria andProbalilityEqualTo(Integer value) {
            addCriterion("probalility =", value, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityNotEqualTo(Integer value) {
            addCriterion("probalility <>", value, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityGreaterThan(Integer value) {
            addCriterion("probalility >", value, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityGreaterThanOrEqualTo(Integer value) {
            addCriterion("probalility >=", value, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityLessThan(Integer value) {
            addCriterion("probalility <", value, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityLessThanOrEqualTo(Integer value) {
            addCriterion("probalility <=", value, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityIn(List<Integer> values) {
            addCriterion("probalility in", values, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityNotIn(List<Integer> values) {
            addCriterion("probalility not in", values, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityBetween(Integer value1, Integer value2) {
            addCriterion("probalility between", value1, value2, "probalility");
            return (Criteria) this;
        }

        public Criteria andProbalilityNotBetween(Integer value1, Integer value2) {
            addCriterion("probalility not between", value1, value2, "probalility");
            return (Criteria) this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("comment is null");
            return (Criteria) this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("comment is not null");
            return (Criteria) this;
        }

        public Criteria andCommentEqualTo(String value) {
            addCriterion("comment =", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotEqualTo(String value) {
            addCriterion("comment <>", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThan(String value) {
            addCriterion("comment >", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(String value) {
            addCriterion("comment >=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThan(String value) {
            addCriterion("comment <", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThanOrEqualTo(String value) {
            addCriterion("comment <=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLike(String value) {
            addCriterion("comment like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotLike(String value) {
            addCriterion("comment not like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentIn(List<String> values) {
            addCriterion("comment in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotIn(List<String> values) {
            addCriterion("comment not in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentBetween(String value1, String value2) {
            addCriterion("comment between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotBetween(String value1, String value2) {
            addCriterion("comment not between", value1, value2, "comment");
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

        public Criteria andAttendNumberIsNull() {
            addCriterion("attend_number is null");
            return (Criteria) this;
        }

        public Criteria andAttendNumberIsNotNull() {
            addCriterion("attend_number is not null");
            return (Criteria) this;
        }

        public Criteria andAttendNumberEqualTo(Integer value) {
            addCriterion("attend_number =", value, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberNotEqualTo(Integer value) {
            addCriterion("attend_number <>", value, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberGreaterThan(Integer value) {
            addCriterion("attend_number >", value, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("attend_number >=", value, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberLessThan(Integer value) {
            addCriterion("attend_number <", value, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberLessThanOrEqualTo(Integer value) {
            addCriterion("attend_number <=", value, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberIn(List<Integer> values) {
            addCriterion("attend_number in", values, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberNotIn(List<Integer> values) {
            addCriterion("attend_number not in", values, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberBetween(Integer value1, Integer value2) {
            addCriterion("attend_number between", value1, value2, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andAttendNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("attend_number not between", value1, value2, "attendNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberIsNull() {
            addCriterion("winning_number is null");
            return (Criteria) this;
        }

        public Criteria andWinningNumberIsNotNull() {
            addCriterion("winning_number is not null");
            return (Criteria) this;
        }

        public Criteria andWinningNumberEqualTo(Integer value) {
            addCriterion("winning_number =", value, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberNotEqualTo(Integer value) {
            addCriterion("winning_number <>", value, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberGreaterThan(Integer value) {
            addCriterion("winning_number >", value, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("winning_number >=", value, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberLessThan(Integer value) {
            addCriterion("winning_number <", value, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberLessThanOrEqualTo(Integer value) {
            addCriterion("winning_number <=", value, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberIn(List<Integer> values) {
            addCriterion("winning_number in", values, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberNotIn(List<Integer> values) {
            addCriterion("winning_number not in", values, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberBetween(Integer value1, Integer value2) {
            addCriterion("winning_number between", value1, value2, "winningNumber");
            return (Criteria) this;
        }

        public Criteria andWinningNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("winning_number not between", value1, value2, "winningNumber");
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