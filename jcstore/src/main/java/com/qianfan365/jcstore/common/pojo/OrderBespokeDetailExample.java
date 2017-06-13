package com.qianfan365.jcstore.common.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderBespokeDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderBespokeDetailExample() {
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

        public Criteria andOrderInfoIdIsNull() {
            addCriterion("order_info_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdIsNotNull() {
            addCriterion("order_info_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdEqualTo(Integer value) {
            addCriterion("order_info_id =", value, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdNotEqualTo(Integer value) {
            addCriterion("order_info_id <>", value, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdGreaterThan(Integer value) {
            addCriterion("order_info_id >", value, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_info_id >=", value, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdLessThan(Integer value) {
            addCriterion("order_info_id <", value, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdLessThanOrEqualTo(Integer value) {
            addCriterion("order_info_id <=", value, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdIn(List<Integer> values) {
            addCriterion("order_info_id in", values, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdNotIn(List<Integer> values) {
            addCriterion("order_info_id not in", values, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdBetween(Integer value1, Integer value2) {
            addCriterion("order_info_id between", value1, value2, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andOrderInfoIdNotBetween(Integer value1, Integer value2) {
            addCriterion("order_info_id not between", value1, value2, "orderInfoId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdIsNull() {
            addCriterion("custom_product_id is null");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdIsNotNull() {
            addCriterion("custom_product_id is not null");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdEqualTo(Integer value) {
            addCriterion("custom_product_id =", value, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdNotEqualTo(Integer value) {
            addCriterion("custom_product_id <>", value, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdGreaterThan(Integer value) {
            addCriterion("custom_product_id >", value, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("custom_product_id >=", value, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdLessThan(Integer value) {
            addCriterion("custom_product_id <", value, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdLessThanOrEqualTo(Integer value) {
            addCriterion("custom_product_id <=", value, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdIn(List<Integer> values) {
            addCriterion("custom_product_id in", values, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdNotIn(List<Integer> values) {
            addCriterion("custom_product_id not in", values, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdBetween(Integer value1, Integer value2) {
            addCriterion("custom_product_id between", value1, value2, "customProductId");
            return (Criteria) this;
        }

        public Criteria andCustomProductIdNotBetween(Integer value1, Integer value2) {
            addCriterion("custom_product_id not between", value1, value2, "customProductId");
            return (Criteria) this;
        }

        public Criteria andImageIsNull() {
            addCriterion("image is null");
            return (Criteria) this;
        }

        public Criteria andImageIsNotNull() {
            addCriterion("image is not null");
            return (Criteria) this;
        }

        public Criteria andImageEqualTo(String value) {
            addCriterion("image =", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotEqualTo(String value) {
            addCriterion("image <>", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThan(String value) {
            addCriterion("image >", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThanOrEqualTo(String value) {
            addCriterion("image >=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThan(String value) {
            addCriterion("image <", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThanOrEqualTo(String value) {
            addCriterion("image <=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLike(String value) {
            addCriterion("image like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotLike(String value) {
            addCriterion("image not like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageIn(List<String> values) {
            addCriterion("image in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotIn(List<String> values) {
            addCriterion("image not in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageBetween(String value1, String value2) {
            addCriterion("image between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotBetween(String value1, String value2) {
            addCriterion("image not between", value1, value2, "image");
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

        public Criteria andMeasureInfoIsNull() {
            addCriterion("measure_info is null");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoIsNotNull() {
            addCriterion("measure_info is not null");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoEqualTo(String value) {
            addCriterion("measure_info =", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoNotEqualTo(String value) {
            addCriterion("measure_info <>", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoGreaterThan(String value) {
            addCriterion("measure_info >", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoGreaterThanOrEqualTo(String value) {
            addCriterion("measure_info >=", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoLessThan(String value) {
            addCriterion("measure_info <", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoLessThanOrEqualTo(String value) {
            addCriterion("measure_info <=", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoLike(String value) {
            addCriterion("measure_info like", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoNotLike(String value) {
            addCriterion("measure_info not like", value, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoIn(List<String> values) {
            addCriterion("measure_info in", values, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoNotIn(List<String> values) {
            addCriterion("measure_info not in", values, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoBetween(String value1, String value2) {
            addCriterion("measure_info between", value1, value2, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andMeasureInfoNotBetween(String value1, String value2) {
            addCriterion("measure_info not between", value1, value2, "measureInfo");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalIsNull() {
            addCriterion("budget_appraisal is null");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalIsNotNull() {
            addCriterion("budget_appraisal is not null");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalEqualTo(Double value) {
            addCriterion("budget_appraisal =", value, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalNotEqualTo(Double value) {
            addCriterion("budget_appraisal <>", value, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalGreaterThan(Double value) {
            addCriterion("budget_appraisal >", value, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalGreaterThanOrEqualTo(Double value) {
            addCriterion("budget_appraisal >=", value, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalLessThan(Double value) {
            addCriterion("budget_appraisal <", value, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalLessThanOrEqualTo(Double value) {
            addCriterion("budget_appraisal <=", value, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalIn(List<Double> values) {
            addCriterion("budget_appraisal in", values, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalNotIn(List<Double> values) {
            addCriterion("budget_appraisal not in", values, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalBetween(Double value1, Double value2) {
            addCriterion("budget_appraisal between", value1, value2, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andBudgetAppraisalNotBetween(Double value1, Double value2) {
            addCriterion("budget_appraisal not between", value1, value2, "budgetAppraisal");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updatetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updatetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("updatetime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("updatetime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("updatetime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updatetime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("updatetime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("updatetime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("updatetime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("updatetime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("updatetime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("updatetime not between", value1, value2, "updatetime");
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