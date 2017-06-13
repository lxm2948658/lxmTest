package com.qianfan365.jcstore.common.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RefundExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RefundExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andMinimalFlagIsNull() {
            addCriterion("minimal_flag is null");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagIsNotNull() {
            addCriterion("minimal_flag is not null");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagEqualTo(Integer value) {
            addCriterion("minimal_flag =", value, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagNotEqualTo(Integer value) {
            addCriterion("minimal_flag <>", value, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagGreaterThan(Integer value) {
            addCriterion("minimal_flag >", value, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("minimal_flag >=", value, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagLessThan(Integer value) {
            addCriterion("minimal_flag <", value, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagLessThanOrEqualTo(Integer value) {
            addCriterion("minimal_flag <=", value, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagIn(List<Integer> values) {
            addCriterion("minimal_flag in", values, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagNotIn(List<Integer> values) {
            addCriterion("minimal_flag not in", values, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagBetween(Integer value1, Integer value2) {
            addCriterion("minimal_flag between", value1, value2, "minimalFlag");
            return (Criteria) this;
        }

        public Criteria andMinimalFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("minimal_flag not between", value1, value2, "minimalFlag");
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

        public Criteria andNumberIsNull() {
            addCriterion("number is null");
            return (Criteria) this;
        }

        public Criteria andNumberIsNotNull() {
            addCriterion("number is not null");
            return (Criteria) this;
        }

        public Criteria andNumberEqualTo(String value) {
            addCriterion("number =", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotEqualTo(String value) {
            addCriterion("number <>", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberGreaterThan(String value) {
            addCriterion("number >", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberGreaterThanOrEqualTo(String value) {
            addCriterion("number >=", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLessThan(String value) {
            addCriterion("number <", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLessThanOrEqualTo(String value) {
            addCriterion("number <=", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLike(String value) {
            addCriterion("number like", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotLike(String value) {
            addCriterion("number not like", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberIn(List<String> values) {
            addCriterion("number in", values, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotIn(List<String> values) {
            addCriterion("number not in", values, "number");
            return (Criteria) this;
        }

        public Criteria andNumberBetween(String value1, String value2) {
            addCriterion("number between", value1, value2, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotBetween(String value1, String value2) {
            addCriterion("number not between", value1, value2, "number");
            return (Criteria) this;
        }

        public Criteria andRefundNumIsNull() {
            addCriterion("refund_num is null");
            return (Criteria) this;
        }

        public Criteria andRefundNumIsNotNull() {
            addCriterion("refund_num is not null");
            return (Criteria) this;
        }

        public Criteria andRefundNumEqualTo(String value) {
            addCriterion("refund_num =", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumNotEqualTo(String value) {
            addCriterion("refund_num <>", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumGreaterThan(String value) {
            addCriterion("refund_num >", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumGreaterThanOrEqualTo(String value) {
            addCriterion("refund_num >=", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumLessThan(String value) {
            addCriterion("refund_num <", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumLessThanOrEqualTo(String value) {
            addCriterion("refund_num <=", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumLike(String value) {
            addCriterion("refund_num like", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumNotLike(String value) {
            addCriterion("refund_num not like", value, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumIn(List<String> values) {
            addCriterion("refund_num in", values, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumNotIn(List<String> values) {
            addCriterion("refund_num not in", values, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumBetween(String value1, String value2) {
            addCriterion("refund_num between", value1, value2, "refundNum");
            return (Criteria) this;
        }

        public Criteria andRefundNumNotBetween(String value1, String value2) {
            addCriterion("refund_num not between", value1, value2, "refundNum");
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

        public Criteria andCustomerIdIsNull() {
            addCriterion("customer_id is null");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIsNotNull() {
            addCriterion("customer_id is not null");
            return (Criteria) this;
        }

        public Criteria andCustomerIdEqualTo(Integer value) {
            addCriterion("customer_id =", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotEqualTo(Integer value) {
            addCriterion("customer_id <>", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdGreaterThan(Integer value) {
            addCriterion("customer_id >", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("customer_id >=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLessThan(Integer value) {
            addCriterion("customer_id <", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLessThanOrEqualTo(Integer value) {
            addCriterion("customer_id <=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIn(List<Integer> values) {
            addCriterion("customer_id in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotIn(List<Integer> values) {
            addCriterion("customer_id not in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdBetween(Integer value1, Integer value2) {
            addCriterion("customer_id between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("customer_id not between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIsNull() {
            addCriterion("customer is null");
            return (Criteria) this;
        }

        public Criteria andCustomerIsNotNull() {
            addCriterion("customer is not null");
            return (Criteria) this;
        }

        public Criteria andCustomerEqualTo(String value) {
            addCriterion("customer =", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerNotEqualTo(String value) {
            addCriterion("customer <>", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerGreaterThan(String value) {
            addCriterion("customer >", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerGreaterThanOrEqualTo(String value) {
            addCriterion("customer >=", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerLessThan(String value) {
            addCriterion("customer <", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerLessThanOrEqualTo(String value) {
            addCriterion("customer <=", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerLike(String value) {
            addCriterion("customer like", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerNotLike(String value) {
            addCriterion("customer not like", value, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerIn(List<String> values) {
            addCriterion("customer in", values, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerNotIn(List<String> values) {
            addCriterion("customer not in", values, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerBetween(String value1, String value2) {
            addCriterion("customer between", value1, value2, "customer");
            return (Criteria) this;
        }

        public Criteria andCustomerNotBetween(String value1, String value2) {
            addCriterion("customer not between", value1, value2, "customer");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andContactWithIsNull() {
            addCriterion("contact_with is null");
            return (Criteria) this;
        }

        public Criteria andContactWithIsNotNull() {
            addCriterion("contact_with is not null");
            return (Criteria) this;
        }

        public Criteria andContactWithEqualTo(String value) {
            addCriterion("contact_with =", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithNotEqualTo(String value) {
            addCriterion("contact_with <>", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithGreaterThan(String value) {
            addCriterion("contact_with >", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithGreaterThanOrEqualTo(String value) {
            addCriterion("contact_with >=", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithLessThan(String value) {
            addCriterion("contact_with <", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithLessThanOrEqualTo(String value) {
            addCriterion("contact_with <=", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithLike(String value) {
            addCriterion("contact_with like", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithNotLike(String value) {
            addCriterion("contact_with not like", value, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithIn(List<String> values) {
            addCriterion("contact_with in", values, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithNotIn(List<String> values) {
            addCriterion("contact_with not in", values, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithBetween(String value1, String value2) {
            addCriterion("contact_with between", value1, value2, "contactWith");
            return (Criteria) this;
        }

        public Criteria andContactWithNotBetween(String value1, String value2) {
            addCriterion("contact_with not between", value1, value2, "contactWith");
            return (Criteria) this;
        }

        public Criteria andRefundTotalIsNull() {
            addCriterion("refund_total is null");
            return (Criteria) this;
        }

        public Criteria andRefundTotalIsNotNull() {
            addCriterion("refund_total is not null");
            return (Criteria) this;
        }

        public Criteria andRefundTotalEqualTo(Double value) {
            addCriterion("refund_total =", value, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalNotEqualTo(Double value) {
            addCriterion("refund_total <>", value, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalGreaterThan(Double value) {
            addCriterion("refund_total >", value, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalGreaterThanOrEqualTo(Double value) {
            addCriterion("refund_total >=", value, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalLessThan(Double value) {
            addCriterion("refund_total <", value, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalLessThanOrEqualTo(Double value) {
            addCriterion("refund_total <=", value, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalIn(List<Double> values) {
            addCriterion("refund_total in", values, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalNotIn(List<Double> values) {
            addCriterion("refund_total not in", values, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalBetween(Double value1, Double value2) {
            addCriterion("refund_total between", value1, value2, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andRefundTotalNotBetween(Double value1, Double value2) {
            addCriterion("refund_total not between", value1, value2, "refundTotal");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounIsNull() {
            addCriterion("receice_amoun is null");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounIsNotNull() {
            addCriterion("receice_amoun is not null");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounEqualTo(Double value) {
            addCriterion("receice_amoun =", value, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounNotEqualTo(Double value) {
            addCriterion("receice_amoun <>", value, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounGreaterThan(Double value) {
            addCriterion("receice_amoun >", value, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounGreaterThanOrEqualTo(Double value) {
            addCriterion("receice_amoun >=", value, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounLessThan(Double value) {
            addCriterion("receice_amoun <", value, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounLessThanOrEqualTo(Double value) {
            addCriterion("receice_amoun <=", value, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounIn(List<Double> values) {
            addCriterion("receice_amoun in", values, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounNotIn(List<Double> values) {
            addCriterion("receice_amoun not in", values, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounBetween(Double value1, Double value2) {
            addCriterion("receice_amoun between", value1, value2, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andReceiceAmounNotBetween(Double value1, Double value2) {
            addCriterion("receice_amoun not between", value1, value2, "receiceAmoun");
            return (Criteria) this;
        }

        public Criteria andRefundFeeIsNull() {
            addCriterion("refund_fee is null");
            return (Criteria) this;
        }

        public Criteria andRefundFeeIsNotNull() {
            addCriterion("refund_fee is not null");
            return (Criteria) this;
        }

        public Criteria andRefundFeeEqualTo(Double value) {
            addCriterion("refund_fee =", value, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeNotEqualTo(Double value) {
            addCriterion("refund_fee <>", value, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeGreaterThan(Double value) {
            addCriterion("refund_fee >", value, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeGreaterThanOrEqualTo(Double value) {
            addCriterion("refund_fee >=", value, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeLessThan(Double value) {
            addCriterion("refund_fee <", value, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeLessThanOrEqualTo(Double value) {
            addCriterion("refund_fee <=", value, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeIn(List<Double> values) {
            addCriterion("refund_fee in", values, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeNotIn(List<Double> values) {
            addCriterion("refund_fee not in", values, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeBetween(Double value1, Double value2) {
            addCriterion("refund_fee between", value1, value2, "refundFee");
            return (Criteria) this;
        }

        public Criteria andRefundFeeNotBetween(Double value1, Double value2) {
            addCriterion("refund_fee not between", value1, value2, "refundFee");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andSalesmanIsNull() {
            addCriterion("salesman is null");
            return (Criteria) this;
        }

        public Criteria andSalesmanIsNotNull() {
            addCriterion("salesman is not null");
            return (Criteria) this;
        }

        public Criteria andSalesmanEqualTo(String value) {
            addCriterion("salesman =", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanNotEqualTo(String value) {
            addCriterion("salesman <>", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanGreaterThan(String value) {
            addCriterion("salesman >", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanGreaterThanOrEqualTo(String value) {
            addCriterion("salesman >=", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanLessThan(String value) {
            addCriterion("salesman <", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanLessThanOrEqualTo(String value) {
            addCriterion("salesman <=", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanLike(String value) {
            addCriterion("salesman like", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanNotLike(String value) {
            addCriterion("salesman not like", value, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanIn(List<String> values) {
            addCriterion("salesman in", values, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanNotIn(List<String> values) {
            addCriterion("salesman not in", values, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanBetween(String value1, String value2) {
            addCriterion("salesman between", value1, value2, "salesman");
            return (Criteria) this;
        }

        public Criteria andSalesmanNotBetween(String value1, String value2) {
            addCriterion("salesman not between", value1, value2, "salesman");
            return (Criteria) this;
        }

        public Criteria andRefundUidIsNull() {
            addCriterion("refund_uid is null");
            return (Criteria) this;
        }

        public Criteria andRefundUidIsNotNull() {
            addCriterion("refund_uid is not null");
            return (Criteria) this;
        }

        public Criteria andRefundUidEqualTo(Integer value) {
            addCriterion("refund_uid =", value, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidNotEqualTo(Integer value) {
            addCriterion("refund_uid <>", value, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidGreaterThan(Integer value) {
            addCriterion("refund_uid >", value, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("refund_uid >=", value, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidLessThan(Integer value) {
            addCriterion("refund_uid <", value, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidLessThanOrEqualTo(Integer value) {
            addCriterion("refund_uid <=", value, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidIn(List<Integer> values) {
            addCriterion("refund_uid in", values, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidNotIn(List<Integer> values) {
            addCriterion("refund_uid not in", values, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidBetween(Integer value1, Integer value2) {
            addCriterion("refund_uid between", value1, value2, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundUidNotBetween(Integer value1, Integer value2) {
            addCriterion("refund_uid not between", value1, value2, "refundUid");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanIsNull() {
            addCriterion("refund_salesman is null");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanIsNotNull() {
            addCriterion("refund_salesman is not null");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanEqualTo(String value) {
            addCriterion("refund_salesman =", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanNotEqualTo(String value) {
            addCriterion("refund_salesman <>", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanGreaterThan(String value) {
            addCriterion("refund_salesman >", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanGreaterThanOrEqualTo(String value) {
            addCriterion("refund_salesman >=", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanLessThan(String value) {
            addCriterion("refund_salesman <", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanLessThanOrEqualTo(String value) {
            addCriterion("refund_salesman <=", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanLike(String value) {
            addCriterion("refund_salesman like", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanNotLike(String value) {
            addCriterion("refund_salesman not like", value, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanIn(List<String> values) {
            addCriterion("refund_salesman in", values, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanNotIn(List<String> values) {
            addCriterion("refund_salesman not in", values, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanBetween(String value1, String value2) {
            addCriterion("refund_salesman between", value1, value2, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundSalesmanNotBetween(String value1, String value2) {
            addCriterion("refund_salesman not between", value1, value2, "refundSalesman");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionIsNull() {
            addCriterion("refund_description is null");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionIsNotNull() {
            addCriterion("refund_description is not null");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionEqualTo(String value) {
            addCriterion("refund_description =", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionNotEqualTo(String value) {
            addCriterion("refund_description <>", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionGreaterThan(String value) {
            addCriterion("refund_description >", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("refund_description >=", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionLessThan(String value) {
            addCriterion("refund_description <", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionLessThanOrEqualTo(String value) {
            addCriterion("refund_description <=", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionLike(String value) {
            addCriterion("refund_description like", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionNotLike(String value) {
            addCriterion("refund_description not like", value, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionIn(List<String> values) {
            addCriterion("refund_description in", values, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionNotIn(List<String> values) {
            addCriterion("refund_description not in", values, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionBetween(String value1, String value2) {
            addCriterion("refund_description between", value1, value2, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andRefundDescriptionNotBetween(String value1, String value2) {
            addCriterion("refund_description not between", value1, value2, "refundDescription");
            return (Criteria) this;
        }

        public Criteria andExpectedDateIsNull() {
            addCriterion("expected_date is null");
            return (Criteria) this;
        }

        public Criteria andExpectedDateIsNotNull() {
            addCriterion("expected_date is not null");
            return (Criteria) this;
        }

        public Criteria andExpectedDateEqualTo(Date value) {
            addCriterionForJDBCDate("expected_date =", value, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("expected_date <>", value, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateGreaterThan(Date value) {
            addCriterionForJDBCDate("expected_date >", value, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("expected_date >=", value, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateLessThan(Date value) {
            addCriterionForJDBCDate("expected_date <", value, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("expected_date <=", value, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateIn(List<Date> values) {
            addCriterionForJDBCDate("expected_date in", values, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("expected_date not in", values, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("expected_date between", value1, value2, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("expected_date not between", value1, value2, "expectedDate");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgIsNull() {
            addCriterion("invoice_img is null");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgIsNotNull() {
            addCriterion("invoice_img is not null");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgEqualTo(String value) {
            addCriterion("invoice_img =", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgNotEqualTo(String value) {
            addCriterion("invoice_img <>", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgGreaterThan(String value) {
            addCriterion("invoice_img >", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgGreaterThanOrEqualTo(String value) {
            addCriterion("invoice_img >=", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgLessThan(String value) {
            addCriterion("invoice_img <", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgLessThanOrEqualTo(String value) {
            addCriterion("invoice_img <=", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgLike(String value) {
            addCriterion("invoice_img like", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgNotLike(String value) {
            addCriterion("invoice_img not like", value, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgIn(List<String> values) {
            addCriterion("invoice_img in", values, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgNotIn(List<String> values) {
            addCriterion("invoice_img not in", values, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgBetween(String value1, String value2) {
            addCriterion("invoice_img between", value1, value2, "invoiceImg");
            return (Criteria) this;
        }

        public Criteria andInvoiceImgNotBetween(String value1, String value2) {
            addCriterion("invoice_img not between", value1, value2, "invoiceImg");
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