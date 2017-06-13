package com.qianfan365.jcstore.common.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class OrderInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderInfoExample() {
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

        public Criteria andOrderStatusIsNull() {
            addCriterion("order_status is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("order_status is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(Integer value) {
            addCriterion("order_status =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(Integer value) {
            addCriterion("order_status <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(Integer value) {
            addCriterion("order_status >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_status >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(Integer value) {
            addCriterion("order_status <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(Integer value) {
            addCriterion("order_status <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<Integer> values) {
            addCriterion("order_status in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<Integer> values) {
            addCriterion("order_status not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(Integer value1, Integer value2) {
            addCriterion("order_status between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("order_status not between", value1, value2, "orderStatus");
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

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(Double value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(Double value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(Double value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(Double value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(Double value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(Double value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<Double> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<Double> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(Double value1, Double value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(Double value1, Double value2) {
            addCriterion("total not between", value1, value2, "total");
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

        public Criteria andAmountAdvancedIsNull() {
            addCriterion("amount_advanced is null");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedIsNotNull() {
            addCriterion("amount_advanced is not null");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedEqualTo(Double value) {
            addCriterion("amount_advanced =", value, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedNotEqualTo(Double value) {
            addCriterion("amount_advanced <>", value, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedGreaterThan(Double value) {
            addCriterion("amount_advanced >", value, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedGreaterThanOrEqualTo(Double value) {
            addCriterion("amount_advanced >=", value, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedLessThan(Double value) {
            addCriterion("amount_advanced <", value, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedLessThanOrEqualTo(Double value) {
            addCriterion("amount_advanced <=", value, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedIn(List<Double> values) {
            addCriterion("amount_advanced in", values, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedNotIn(List<Double> values) {
            addCriterion("amount_advanced not in", values, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedBetween(Double value1, Double value2) {
            addCriterion("amount_advanced between", value1, value2, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andAmountAdvancedNotBetween(Double value1, Double value2) {
            addCriterion("amount_advanced not between", value1, value2, "amountAdvanced");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNull() {
            addCriterion("discount is null");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNotNull() {
            addCriterion("discount is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountEqualTo(Double value) {
            addCriterion("discount =", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotEqualTo(Double value) {
            addCriterion("discount <>", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThan(Double value) {
            addCriterion("discount >", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThanOrEqualTo(Double value) {
            addCriterion("discount >=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThan(Double value) {
            addCriterion("discount <", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThanOrEqualTo(Double value) {
            addCriterion("discount <=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountIn(List<Double> values) {
            addCriterion("discount in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotIn(List<Double> values) {
            addCriterion("discount not in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountBetween(Double value1, Double value2) {
            addCriterion("discount between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotBetween(Double value1, Double value2) {
            addCriterion("discount not between", value1, value2, "discount");
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

        public Criteria andOrderDateIsNull() {
            addCriterion("order_date is null");
            return (Criteria) this;
        }

        public Criteria andOrderDateIsNotNull() {
            addCriterion("order_date is not null");
            return (Criteria) this;
        }

        public Criteria andOrderDateEqualTo(Date value) {
            addCriterion("order_date =", value, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateNotEqualTo(Date value) {
            addCriterion("order_date <>", value, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateGreaterThan(Date value) {
            addCriterion("order_date >", value, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateGreaterThanOrEqualTo(Date value) {
            addCriterion("order_date >=", value, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateLessThan(Date value) {
            addCriterion("order_date <", value, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateLessThanOrEqualTo(Date value) {
            addCriterion("order_date <=", value, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateIn(List<Date> values) {
            addCriterion("order_date in", values, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateNotIn(List<Date> values) {
            addCriterion("order_date not in", values, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateBetween(Date value1, Date value2) {
            addCriterion("order_date between", value1, value2, "orderDate");
            return (Criteria) this;
        }

        public Criteria andOrderDateNotBetween(Date value1, Date value2) {
            addCriterion("order_date not between", value1, value2, "orderDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateIsNull() {
            addCriterion("expected_delivery_date is null");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateIsNotNull() {
            addCriterion("expected_delivery_date is not null");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateEqualTo(Date value) {
            addCriterionForJDBCDate("expected_delivery_date =", value, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("expected_delivery_date <>", value, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateGreaterThan(Date value) {
            addCriterionForJDBCDate("expected_delivery_date >", value, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("expected_delivery_date >=", value, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateLessThan(Date value) {
            addCriterionForJDBCDate("expected_delivery_date <", value, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("expected_delivery_date <=", value, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateIn(List<Date> values) {
            addCriterionForJDBCDate("expected_delivery_date in", values, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("expected_delivery_date not in", values, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("expected_delivery_date between", value1, value2, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andExpectedDeliveryDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("expected_delivery_date not between", value1, value2, "expectedDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andCancelReasonIsNull() {
            addCriterion("cancel_reason is null");
            return (Criteria) this;
        }

        public Criteria andCancelReasonIsNotNull() {
            addCriterion("cancel_reason is not null");
            return (Criteria) this;
        }

        public Criteria andCancelReasonEqualTo(String value) {
            addCriterion("cancel_reason =", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonNotEqualTo(String value) {
            addCriterion("cancel_reason <>", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonGreaterThan(String value) {
            addCriterion("cancel_reason >", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonGreaterThanOrEqualTo(String value) {
            addCriterion("cancel_reason >=", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonLessThan(String value) {
            addCriterion("cancel_reason <", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonLessThanOrEqualTo(String value) {
            addCriterion("cancel_reason <=", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonLike(String value) {
            addCriterion("cancel_reason like", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonNotLike(String value) {
            addCriterion("cancel_reason not like", value, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonIn(List<String> values) {
            addCriterion("cancel_reason in", values, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonNotIn(List<String> values) {
            addCriterion("cancel_reason not in", values, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonBetween(String value1, String value2) {
            addCriterion("cancel_reason between", value1, value2, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andCancelReasonNotBetween(String value1, String value2) {
            addCriterion("cancel_reason not between", value1, value2, "cancelReason");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeIsNull() {
            addCriterion("refunding_fee is null");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeIsNotNull() {
            addCriterion("refunding_fee is not null");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeEqualTo(Double value) {
            addCriterion("refunding_fee =", value, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeNotEqualTo(Double value) {
            addCriterion("refunding_fee <>", value, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeGreaterThan(Double value) {
            addCriterion("refunding_fee >", value, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeGreaterThanOrEqualTo(Double value) {
            addCriterion("refunding_fee >=", value, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeLessThan(Double value) {
            addCriterion("refunding_fee <", value, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeLessThanOrEqualTo(Double value) {
            addCriterion("refunding_fee <=", value, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeIn(List<Double> values) {
            addCriterion("refunding_fee in", values, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeNotIn(List<Double> values) {
            addCriterion("refunding_fee not in", values, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeBetween(Double value1, Double value2) {
            addCriterion("refunding_fee between", value1, value2, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundingFeeNotBetween(Double value1, Double value2) {
            addCriterion("refunding_fee not between", value1, value2, "refundingFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeIsNull() {
            addCriterion("refunded_fee is null");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeIsNotNull() {
            addCriterion("refunded_fee is not null");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeEqualTo(Double value) {
            addCriterion("refunded_fee =", value, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeNotEqualTo(Double value) {
            addCriterion("refunded_fee <>", value, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeGreaterThan(Double value) {
            addCriterion("refunded_fee >", value, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeGreaterThanOrEqualTo(Double value) {
            addCriterion("refunded_fee >=", value, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeLessThan(Double value) {
            addCriterion("refunded_fee <", value, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeLessThanOrEqualTo(Double value) {
            addCriterion("refunded_fee <=", value, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeIn(List<Double> values) {
            addCriterion("refunded_fee in", values, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeNotIn(List<Double> values) {
            addCriterion("refunded_fee not in", values, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeBetween(Double value1, Double value2) {
            addCriterion("refunded_fee between", value1, value2, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andRefundedFeeNotBetween(Double value1, Double value2) {
            addCriterion("refunded_fee not between", value1, value2, "refundedFee");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagIsNull() {
            addCriterion("signature_flag is null");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagIsNotNull() {
            addCriterion("signature_flag is not null");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagEqualTo(Integer value) {
            addCriterion("signature_flag =", value, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagNotEqualTo(Integer value) {
            addCriterion("signature_flag <>", value, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagGreaterThan(Integer value) {
            addCriterion("signature_flag >", value, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("signature_flag >=", value, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagLessThan(Integer value) {
            addCriterion("signature_flag <", value, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagLessThanOrEqualTo(Integer value) {
            addCriterion("signature_flag <=", value, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagIn(List<Integer> values) {
            addCriterion("signature_flag in", values, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagNotIn(List<Integer> values) {
            addCriterion("signature_flag not in", values, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagBetween(Integer value1, Integer value2) {
            addCriterion("signature_flag between", value1, value2, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("signature_flag not between", value1, value2, "signatureFlag");
            return (Criteria) this;
        }

        public Criteria andSignatureImgIsNull() {
            addCriterion("signature_img is null");
            return (Criteria) this;
        }

        public Criteria andSignatureImgIsNotNull() {
            addCriterion("signature_img is not null");
            return (Criteria) this;
        }

        public Criteria andSignatureImgEqualTo(String value) {
            addCriterion("signature_img =", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgNotEqualTo(String value) {
            addCriterion("signature_img <>", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgGreaterThan(String value) {
            addCriterion("signature_img >", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgGreaterThanOrEqualTo(String value) {
            addCriterion("signature_img >=", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgLessThan(String value) {
            addCriterion("signature_img <", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgLessThanOrEqualTo(String value) {
            addCriterion("signature_img <=", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgLike(String value) {
            addCriterion("signature_img like", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgNotLike(String value) {
            addCriterion("signature_img not like", value, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgIn(List<String> values) {
            addCriterion("signature_img in", values, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgNotIn(List<String> values) {
            addCriterion("signature_img not in", values, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgBetween(String value1, String value2) {
            addCriterion("signature_img between", value1, value2, "signatureImg");
            return (Criteria) this;
        }

        public Criteria andSignatureImgNotBetween(String value1, String value2) {
            addCriterion("signature_img not between", value1, value2, "signatureImg");
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

        public Criteria andAdjunctImgIsNull() {
            addCriterion("adjunct_img is null");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgIsNotNull() {
            addCriterion("adjunct_img is not null");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgEqualTo(String value) {
            addCriterion("adjunct_img =", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgNotEqualTo(String value) {
            addCriterion("adjunct_img <>", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgGreaterThan(String value) {
            addCriterion("adjunct_img >", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgGreaterThanOrEqualTo(String value) {
            addCriterion("adjunct_img >=", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgLessThan(String value) {
            addCriterion("adjunct_img <", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgLessThanOrEqualTo(String value) {
            addCriterion("adjunct_img <=", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgLike(String value) {
            addCriterion("adjunct_img like", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgNotLike(String value) {
            addCriterion("adjunct_img not like", value, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgIn(List<String> values) {
            addCriterion("adjunct_img in", values, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgNotIn(List<String> values) {
            addCriterion("adjunct_img not in", values, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgBetween(String value1, String value2) {
            addCriterion("adjunct_img between", value1, value2, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andAdjunctImgNotBetween(String value1, String value2) {
            addCriterion("adjunct_img not between", value1, value2, "adjunctImg");
            return (Criteria) this;
        }

        public Criteria andProofIsNull() {
            addCriterion("proof is null");
            return (Criteria) this;
        }

        public Criteria andProofIsNotNull() {
            addCriterion("proof is not null");
            return (Criteria) this;
        }

        public Criteria andProofEqualTo(String value) {
            addCriterion("proof =", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofNotEqualTo(String value) {
            addCriterion("proof <>", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofGreaterThan(String value) {
            addCriterion("proof >", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofGreaterThanOrEqualTo(String value) {
            addCriterion("proof >=", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofLessThan(String value) {
            addCriterion("proof <", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofLessThanOrEqualTo(String value) {
            addCriterion("proof <=", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofLike(String value) {
            addCriterion("proof like", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofNotLike(String value) {
            addCriterion("proof not like", value, "proof");
            return (Criteria) this;
        }

        public Criteria andProofIn(List<String> values) {
            addCriterion("proof in", values, "proof");
            return (Criteria) this;
        }

        public Criteria andProofNotIn(List<String> values) {
            addCriterion("proof not in", values, "proof");
            return (Criteria) this;
        }

        public Criteria andProofBetween(String value1, String value2) {
            addCriterion("proof between", value1, value2, "proof");
            return (Criteria) this;
        }

        public Criteria andProofNotBetween(String value1, String value2) {
            addCriterion("proof not between", value1, value2, "proof");
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