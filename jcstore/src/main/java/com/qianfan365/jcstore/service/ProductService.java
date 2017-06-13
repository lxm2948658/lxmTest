package com.qianfan365.jcstore.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.check.ProductCheck;
import com.qianfan365.jcstore.common.constant.ProductStatusConstant;
import com.qianfan365.jcstore.common.pojo.*;
import com.qianfan365.jcstore.common.pojo.ProductExample.Criteria;
import com.qianfan365.jcstore.common.util.POIUtils;
import com.qianfan365.jcstore.dao.inter.ClientMapper;
import com.qianfan365.jcstore.dao.inter.ProductGroupMapper;
import com.qianfan365.jcstore.dao.inter.ProductMapper;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("productService")
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionInfoService permissionInfoService;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductGroupMapper productGroupMapper;
    @Autowired
    private MarketingService marketingService;

    /**
     * 增加商品
     *
     * @param product
     * @return
     */
    @Transactional
    public int addProduct(Product product, Integer uid, Integer belongs, Integer shopId) {
        Date date = new Date();
        if (product.getCustomDescription() != null) {
            JSONObject jsonObject = JSONObject.parseObject(product.getCustomDescription());
            JSONArray jsonArray = jsonObject.getJSONArray("customDescription");
            if (jsonArray.size() > 5) {// 自定义条数不能超过五条
                return 0;
            }
            for (int i = 0; jsonArray.size() > i; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.get("info") != null
                    && jsonObject.get("info").toString().length() > 20) {
                    return 0;
                }
                if (jsonObject.get("name").toString().length() > 8) {
                    return 0;
                }
            }
        }
        if (product.getId() == null) {// 新增
            product.setUserId(uid);
            product.setShopId(shopId);
            product.setStatus(ProductStatusConstant.Yes);
            product.setCreatetime(date);
            product.setUpdatetime(date);
            // 1.1期修改,成本价和库存默认为0
            if (product.getCostPrice() == null) {
                product.setCostPrice(0.0);
            }
            if (product.getInventory() == null) {
                product.setInventory(999999);
            }
            return productMapper.insertSelective(product);
        } else {
            Product pro = productMapper.selectByPrimaryKey(product.getId());
            if (pro == null) {
                return Status.PARAMETER_ERROR.code;
            }
            if (uid.equals(pro.getUserId())) { // 是自己下的单
                return updateProductWhenAdd(product);
            }
            User proUser = userService.findUser(pro.getUserId()); // 获取创建商品的人
            if (proUser == null) {
                return Status.PARAMETER_ERROR.code;
            }
            Integer proBelongs = proUser.getBelongs() == 0 ? proUser.getId() : proUser.getBelongs();
            if (belongs == 0 && uid.equals(proBelongs)) { // 如果是店主 且 是本店的人下的单
                return updateProductWhenAdd(product);
            }
            if (belongs.equals(proBelongs)) { // 如果是员工 // 是本店的人下的单
                return updateProductWhenAdd(product);
            }

            return -1;
        }

    }

    /**
     * 1.1期 ,新增或更新商品方法
     *
     * @param product
     * @param uid
     * @param belongs
     * @param shopId
     * @return
     */
    @Transactional
    public int add(Product product, Integer uid, Integer belongs, Integer shopId) {
        Date date = new Date();
        // 自定义描述校验部分,校验存储的内容是否符合约定的规则
        if (product.getCustomDescription() != null) {
            JSONObject jsonObject = JSONObject.parseObject(product.getCustomDescription());
            JSONArray jsonArray = jsonObject.getJSONArray("customDescription");
            if (jsonArray.size() > 5) {// 自定义条数不能超过五条
                return 0;
            }
            for (int i = 0; jsonArray.size() > i; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.get("info") != null
                    && jsonObject.get("info").toString().length() > 20) {
                    return 0;
                }
                if (jsonObject.get("name").toString().length() > 8) {
                    return 0;
                }
            }
        }
        // ID为空时新增
        if (product.getId() == null) {
            product.setUserId(uid);
            product.setShopId(shopId);
            product.setStatus(ProductStatusConstant.Yes);
            product.setCreatetime(date);
            product.setUpdatetime(date);
            if (product.getCostPrice() == null) {
                product.setCostPrice(0.0);
            }
            if (product.getInventory() == null) {
                product.setInventory(999999);
            }
            return productMapper.insertSelective(product);
        } else {
            // 更新商品
            // 根据ID获取商品对象
            Product pro = productMapper.selectByPrimaryKey(product.getId());
            // 对应的商品不存在
            if (pro == null) {
                return Status.PARAMETER_ERROR.code;
            }
            // 判断是否是自己添加的商品
            if (uid.equals(pro.getUserId())) { // 是自己下的单
                return updateProductWhileAdd(product);
            }
            // 判断添加此商品的对象是否存在
            User proUser = userService.findUser(pro.getUserId()); // 获取创建商品的人
            if (proUser == null) {
                return Status.PARAMETER_ERROR.code;
            }
            // 更新逻辑
            Integer proBelongs = proUser.getBelongs() == 0 ? proUser.getId() : proUser.getBelongs();
            if (belongs == 0 && uid.equals(proBelongs)) { // 如果是店主 且 是本店的人下的单
                return updateProductWhileAdd(product);
            }
            if (belongs.equals(proBelongs)) { // 如果是员工 // 是本店的人下的单
                return updateProductWhileAdd(product);
            }
            return -1;
        }

    }

    /**
     * 1.1期新增,用于商品更新
     *
     * @param product
     * @return
     */
    private int updateProductWhileAdd(Product product) {
        product.setUpdatetime(new Date());
        // 1.1期,库存和成本价不传时默认为0
        if (product.getCostPrice() == null) {
            product.setCostPrice(0.0);
        }
        if (product.getInventory() == null) {
            product.setInventory(999999);
        }
        int updateByExampleSelective = productMapper.updateByPrimaryKeySelective(product);
        // 条形码为空时置空
        if (StringUtils.isEmpty(product.getBarCode())) {
            productMapper.updateBarCodeToNull(product.getId());
        }
        // 分组为空时置空
        if (product.getGroupId() == null) {
            this.productMapper.updateGroupToNull(product.getId());
        }
        marketingService.updateProductName(product);
        return updateByExampleSelective;
    }

    /**
     * 易开单1期接口,更新商品,已修改成本价和库存默认值均为0,不可置空
     *
     * @param product
     * @return
     */
    private int updateProductWhenAdd(Product product) {
        product.setUpdatetime(new Date());
        if (product.getCostPrice() == null) {
            product.setCostPrice(0.0);
        }
        if (product.getInventory() == null) {
            product.setInventory(999999);
        }
        int updateByExampleSelective = productMapper.updateByPrimaryKeySelective(product);
        return updateByExampleSelective;
    }


    /**
     * 删除商品(假删)
     *
     * @param productId
     * @param uid
     * @return
     */
    @Transactional
    public int delProduct(Integer productId, List<Integer> uid) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andUserIdIn(uid).andIdEqualTo(productId).andStatusEqualTo(ProductStatusConstant.Yes);
        Product product = new Product();
        product.setStatus(ProductStatusConstant.No);
        product.setUpdatetime(new Date());
        return productMapper.updateByExampleSelective(product, productExample);

    }

    /**
     * 修改商品
     *
     * @param product
     * @param uid
     * @param shopId
     * @return
     */
    @Transactional
    public int updateProduct(Product product, Integer uid) {
        Date date = new Date();
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andUserIdEqualTo(uid).andIdEqualTo(product.getId());
        product.setUpdatetime(date);
        return productMapper.updateByExampleSelective(product, productExample);
    }

    /**
     * 根据商品ID查询未删除商品
     *
     * @param productId
     * @return
     */
    public Product findByPidProduct(Integer productId, List<Integer> uid) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andIdEqualTo(productId).andStatusEqualTo(ProductStatusConstant.Yes).andUserIdIn(uid);
        List<Product> product = productMapper.selectByExample(productExample);
        if (product.isEmpty()) {
            return null;
        }
        return productMapper.selectByExample(productExample).get(0);
    }

    /**
     * 根据商品ID查询存在的商品
     *
     * @param productId
     * @return
     */
    public List<Product> findByPidProduct(List<Integer> productId) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andIdIn(productId).andStatusEqualTo(ProductStatusConstant.Yes);
        return productMapper.selectByExample(productExample);

    }

    /**
     * 返回给定商品id集合中不存在的商品ID
     *
     * @param productId
     * @return
     */
    public List<Integer> findByPidProductWithIds(List<Integer> productId) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andIdIn(productId).andStatusEqualTo(ProductStatusConstant.No);
        List<Product> product = productMapper.selectByExample(productExample);
        return product.stream().mapToInt(Product::getId).boxed().collect(Collectors.toList());
    }

    /**
     * 根据给定商品id集合返回商品
     *
     * @param productId
     * @return
     */
    public List<Product> findByIds(List<Integer> pids, Integer shopId) {
        List<Product> productList =
            productMapper.selectByIds(pids.toString().replaceAll("[\\[\\]]", ""), shopId);
        ArrayList<Product> list = new ArrayList<Product>();
        productList.stream().forEach(product -> list.add(new ProductBean(product)));
        return list;
    }

    /**
     * 根据ID查询商品所有
     *
     * @param productId
     * @return
     */
    public List<Product> findByPid(Integer productId) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andIdEqualTo(productId);
        // List<Product> list = productMapper.selectByExample(productExample);
        return productMapper.selectByExample(productExample);
    }

    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    public Product findById(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据商品名称及用户ID列表查询商品列表
     *
     * @param name
     * @param orderType
     * @param groupId
     * @return
     */
    public Page<ProductBean> find(Integer currentPage, Integer limit, String name, Integer shopId,
          Integer groupId, Integer orderType, Long querytime) {
        PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
        ProductExample productExample = new ProductExample();
        // 默认更新时间倒序排列
        if (orderType == 1) {
            productExample.setOrderByClause(" sale_price desc");
        } else if (orderType == 2) {
            productExample.setOrderByClause(" sale_price asc");
        } else if (orderType == 3) {
            // 1.5新增 库存升降序排列
            productExample.setOrderByClause(" inventory desc,updatetime desc");
        } else if (orderType == 4) {
            productExample.setOrderByClause(" inventory asc,updatetime desc");
        } else {
            productExample.setOrderByClause(" updatetime desc");
        }
        Criteria criteria = productExample.createCriteria();
        // 商品属于当前店铺并且未被删除
        criteria.andShopIdEqualTo(shopId).andStatusEqualTo(ProductStatusConstant.Yes);
        if(null != querytime && 0 != querytime){
          criteria.andCreatetimeLessThanOrEqualTo(new Date(querytime));
        }
        if (groupId != null) {
            criteria.andGroupIdEqualTo(groupId);
        }
        // 如需模糊查询商品名称或条形码,则添加条件
        if (StringUtils.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
            Criteria or = productExample.or();
            or.andBarCodeEqualTo(name).andShopIdEqualTo(shopId).andStatusEqualTo(ProductStatusConstant.Yes);
            if(null != querytime && 0 != querytime){
              or.andCreatetimeLessThanOrEqualTo(new Date(querytime));
            }
            if (groupId != null) {
              or.andGroupIdEqualTo(groupId);
            }
        }
        return convertToBean((Page<Product>) productMapper.selectByExample(productExample));
    }

    /**
     * 根据商品名字以及用户ID查询
     *
     * @param name
     * @return
     */
    public Page<Product> findByName(Integer currentPage, Integer limit, String name, List<Integer> uid) {
        PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andNameLike(
            "%" + name + "%").andUserIdIn(uid).andStatusEqualTo(ProductStatusConstant.Yes);
        productExample.setOrderByClause("updatetime desc");
        return (Page<Product>) productMapper.selectByExample(productExample);
    }


    /**
     * 根据用户查询所有商品
     */
    public Page<ProductBean> findByUidProduct(Integer currentPage, Integer limit, List<Integer> uid, Integer groupId) {
        PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
        ProductExample productExample = new ProductExample();
        Criteria cc = productExample.createCriteria();
        cc.andUserIdIn(uid).andStatusEqualTo(ProductStatusConstant.Yes);
        if (groupId != null) {
            cc.andGroupIdEqualTo(groupId);
        }
        productExample.setOrderByClause("updatetime desc");
        return convertToBean((Page<Product>) productMapper.selectByExample(productExample));
    }

    /**
     * 转换为bean
     *
     * @param pageProduct
     * @return
     */
    private Page<ProductBean> convertToBean(Page<Product> pageProduct) {
        Page<ProductBean> page = new Page<ProductBean>();
        page.setPageNum(pageProduct.getPageNum());
        page.setPageSize(pageProduct.getPageSize());
        page.setTotal(pageProduct.getTotal());
        pageProduct.stream().forEach(product -> page.add(new ProductBean(product)));
        return page;
    }

    /**
     * 分组删除 修改商品分组为空
     *
     * @param groupId
     * @param uid
     * @return
     */
    @Transactional
    public int updateByGroup(Integer groupId) {
        return productMapper.updateGroup(groupId);
    }

    /**
     * 查询所有商品
     *
     * @param currentPage
     * @param limit
     * @return
     */
    public Page<Product> findAllProduct(Integer currentPage, Integer limit) {
        PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andIdIsNotNull();
        return (Page<Product>) productMapper.selectByExample(productExample);

    }

    /**
     * 根据分组查询商品列表
     *
     * @param currentPage
     * @param limit
     * @param groupId
     * @return
     */
    public Page<Product> findGroupProduct(Integer currentPage, Integer limit, Integer groupId) {
        PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andGroupIdEqualTo(groupId).andStatusEqualTo(ProductStatusConstant.Yes);
        productExample.setOrderByClause("updatetime desc");
        return (Page<Product>) productMapper.selectByExample(productExample);
    }

    /**
     * 校验条形码是否重复
     *
     * @param barCode
     * @param pid
     * @param shopId
     * @return
     */
    public boolean checkBarCode(String barCode, Integer pid, Integer shopId) {
        // 校验
        ProductExample example = new ProductExample();
        Criteria criteria = example.createCriteria();
        // 同店铺下,已存在的商品中是否有相同的条形码
        criteria.andShopIdEqualTo(shopId).andStatusEqualTo(ProductStatusConstant.Yes).andBarCodeEqualTo(barCode);
        // 更新时,校验除此商品外其他商品
        if (pid != null)
            criteria.andIdNotEqualTo(pid);
        // 查询
        List<Product> list = this.productMapper.selectByExample(example);
        if (list != null) {
            return list.isEmpty();
        }
        return true;
    }

    /**
     * 根据条形码获取商品
     *
     * @param barCode
     * @param userIds
     * @return
     */
    public Product findByBarCode(String barCode, Integer shopId) {
        ProductExample example = new ProductExample();
        // 根据条形码查找当前店铺下的商品
        example.createCriteria().andBarCodeEqualTo(barCode).andShopIdEqualTo(shopId).andStatusEqualTo(ProductStatusConstant.Yes);
        List<Product> list = this.productMapper.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * 根据关键字获取商品
     *
     * @param barCode
     * @param userIds
     * @return
     */
    public Product findByKeyword(String keyword, Integer shopId) {
        ProductExample example = new ProductExample();
        example.setOrderByClause(" updatetime desc");
        // 根据条形码查找当前店铺下的商品
        example.createCriteria().andKeywordEqualTo(keyword).andShopIdEqualTo(shopId).andStatusEqualTo(ProductStatusConstant.Yes);
        List<Product> list = this.productMapper.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 更改库存
     *
     * @param pid
     * @param inventory
     * @return
     */
    public int updateInventory(Integer pid, Integer inventory) {
        return productMapper.updateInventory(pid, inventory);
    }
    
    /**
     * 导入商品
     *
     * @param file
     * @param clientId
     */
    public ResultData importProduct(MultipartFile file, Integer clientId) {
        ResultData map = ResultData.build();
        // 获取新增商品时所需的前台信息
        Client client = clientMapper.selectByPrimaryKey(clientId);
        if (client == null) {
            return map.data404();
        }
        // 获取对应的用户ID及店铺ID
        User user = userService.findUser(client.getUid());
        Integer uid = user.getId();
        Integer shopId = shopService.findShop(user).getId();
        // 获取文件名
        String name = file.getOriginalFilename();
        String fileExtName = name.substring(name.lastIndexOf("."), name.length());
        // 获取条形码集合
        List<String> barCodes = productMapper.getAllBarCode(shopId);
        try {
            InputStream inputStream = file.getInputStream();
            // 读取xlsx文件
            Workbook workbook = null;
            if (".xlsx".equals(fileExtName)) {
                try {
                    workbook = new XSSFWorkbook(inputStream);
                } catch (Exception e) {
                    return map.parameterError().put("statusMsg", "导入失败");
                }
            } else {
                return map.setStatus(Status.FILE_FORMAT_ERROR).put("statusMsg", "文件格式错误！");
            }
            // 只读取第一工作单元内容
            Sheet sheet = workbook.getSheetAt(0);
            // 实际数据字段行数
            int rowCnt = sheet.getLastRowNum() + 1;
            // 校验行数不可超过1000
            if (rowCnt > 301) {
                for (int extCnt = 301; extCnt < rowCnt; extCnt++) {
                    if (POIUtils.isEmpty(sheet.getRow(extCnt))) {
                        return map.setStatus(Status.IMPORT_NUMBER_OVERAGE).put("statusMsg", "导入失败，导入数据不超过300行！");
                    }
                }
            }
            // 记录错误数据行号
            List<Integer> dataArray = Lists.newArrayList();
            // 记录错误正确数据
            List<Product> productArray = Lists.newArrayList();
            // 当前日期
            Date nowTime = new Date();
            // 循环读取内容
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // 封装数据
                try {
                    Row row = sheet.getRow(i);
                    if (!POIUtils.isEmpty(row)) {
                        continue;
                    }
                    Product product = this.getInfoFromRow(row, uid, barCodes);
                    product.setCustomDescription("{\"customDescription\":[]}");
                    // 封装信息
                    product.setUserId(uid);
                    product.setShopId(shopId);
                    product.setStatus(ProductStatusConstant.Yes);
                    product.setCreatetime(nowTime);
                    product.setUpdatetime(nowTime);
                    // 1.1期修改,成本价和库存默认为0
                    if (product.getCostPrice() == null) {
                        product.setCostPrice(0.0);
                    }
                    if (product.getInventory() == null) {
                        product.setInventory(999999);
                    }
                    productArray.add(product);
                } catch (Exception e) {
                    dataArray.add(i + 1);
                }
            }
            // 未通过校验
            if (dataArray.size() != 0) {
                map.setStatus(Status.PRODUCT_FORMAT_ERROR).put("errorRowList", dataArray);
            } else {
                // 保存商品
                for (int i = 0; i < productArray.size(); i = i + 30) {
                    productMapper.insertBatch(productArray.subList(i,
                        i + 30 > productArray.size() ? productArray.size() : i + 30));
                }
                map.put("successNum", productArray.size());
            }
            return map;
        } catch (IOException e) {
            return map.parameterError().put("statusMsg", "导入失败");
        }
    }

    /**
     * 读取单条记录信息
     *
     * @param row
     * @param shopId
     * @param uid
     * @param barCodes
     * @param dataArray
     * @return
     */
    private Product getInfoFromRow(Row row, Integer uid, List<String> barCodes) throws Exception {
        // 读取此条数据内容
        Product product = new Product();
        // 依次封装参数
        product.importName(POIUtils.getCellValue(row.getCell(0)));
        product.importCode(POIUtils.getCellValue(row.getCell(1)));
        product.importBarCode(POIUtils.getCellValue(row.getCell(2)));
        product.importStandard(POIUtils.getCellValue(row.getCell(4)));
        String value = POIUtils.getCellValue(row.getCell(5));
        // 成本价或库存可为空
        product.setCostPrice(StringUtils.isEmpty(value) ? null : Double.parseDouble(value));
        product.setSalePrice(Double.parseDouble(POIUtils.getCellValue(row.getCell(6))));
        String cellValue = POIUtils.getCellValue(row.getCell(7));
        product.setInventory(StringUtils.isEmpty(cellValue) ? null : Integer.parseInt(cellValue));
        // 封装分组信息,只有分组名称完全相同时才保存此信息
        product.setGroupId(productGroupMapper.getGroupIdByUidAndName(POIUtils.getCellValue(row.getCell(3)), uid));
        Boolean check = ProductCheck.saveProductCheck(product);
        // 条形码校验
        boolean barCode_check = false;
        if (StringUtils.isNotEmpty(product.getBarCode())) {
            barCode_check = barCodes.contains(product.getBarCode());
        }
        if (check || barCode_check) {
            throw new RuntimeException();
        }
        barCodes.add(product.getBarCode());
        return product;
    }

    /**
     * 校验体验账号的商品数量是否已经超出
     *
     * @param uid
     * @return
     */
    public boolean checkTrialProductNum(Integer uid) {
        // 因为体验账号不能添加员工,所以直接使用uid查询
        ProductExample example = new ProductExample();
        example.createCriteria().andUserIdEqualTo(uid).andStatusEqualTo(ProductStatusConstant.Yes);
        return productMapper.countByExample(example) >= 10;
    }

    public void generateDefaultProduct(User user) {
        Integer shopId = shopService.findShop(user).getId();
        Product productA = new Product();
        productA.setName("旗航壁纸电视背景墙壁纸3d厚浮雕客厅卧室壁纸环保无纺布墙");
        productA.setImage("https://cdn.yikaidan.cn/jcstore/talkart/1480402001452_79418665.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402033178_62635570.jpg");
        productA.setCode("QH-00033");
        productA.setStatus(1);
        productA.setStandard("绿色");
        productA.setCostPrice(85.0);
        productA.setSalePrice(158.0);
        productA.setInventory(4218);
        productA.setShopId(shopId);
        productA.setUserId(user.getId());
        productA.setCustomDescription("{\"customDescription\":[{\"name\":\"面层工艺\",\"info\":\"浮雕\"}]}");
        productA.setCreatetime(new Date());
        productA.setUpdatetime(new Date());
        Product productB = new Product();
        productB.setName("东鹏瓷砖 臻皇玉 全抛釉客厅卧室瓷砖背景墙地板砖玻化砖800砖");
        productB.setImage("https://cdn.yikaidan.cn/jcstore/talkart/1480402054783_69562532.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402076376_60754356.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402095787_51401784.jpg");
        productB.setCode("800EFG20002");
        productB.setStatus(1);
        productB.setStandard("800*800");
        productB.setCostPrice(89.0);
        productB.setSalePrice(294.0);
        productB.setInventory(98479);
        productB.setShopId(shopId);
        productB.setUserId(user.getId());
        productB.setCustomDescription("{\"customDescription\":[{\"name\":\"风格\",\"info\":\"现代简约\"}]}");
        productB.setCreatetime(new Date());
        productB.setUpdatetime(new Date());
        Product productC = new Product();
        productC.setName("Mexin美心木门钢木门室内门卧室门静音门免漆门简欧木门");
        productC.setImage("https://cdn.yikaidan.cn/jcstore/talkart/1480402114478_1180741.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402149896_61035178.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402164979_28231755.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402178452_11296083.jpg");
        productC.setCode("2156");
        productC.setStatus(1);
        productC.setStandard("F211-白木纹");
        productC.setCostPrice(2598.0);
        productC.setSalePrice(1280.0);
        productC.setInventory(294);
        productC.setShopId(shopId);
        productC.setUserId(user.getId());
        productC.setCustomDescription("{\"customDescription\":[{\"name\":\"饰面工艺\",\"info\":\"免漆\"}]}");
        productC.setCreatetime(new Date());
        productC.setUpdatetime(new Date());
        Product productD = new Product();
        productD.setName("箭牌卫浴总裁特批套餐头牌马桶AB1116浴室柜组合AE2502花洒AE3309");
        productD.setImage("https://cdn.yikaidan.cn/jcstore/talkart/1480930583680_27493636.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402209543_37216794.jpg");
        productD.setCode("AB1116+AE2502+AE3309");
        productD.setStatus(1);
        productD.setStandard("马桶AB1116浴室柜组合AE2502");
        productD.setCostPrice(1999.0);
        productD.setSalePrice(3199.0);
        productD.setInventory(440);
        productD.setShopId(shopId);
        productD.setUserId(user.getId());
        productD.setCustomDescription("{\"customDescription\":[{\"name\":\"组合形式\",\"info\":\"含带浴室边柜、浴室镜柜、配套面盆\"}]}");
        productD.setCreatetime(new Date());
        productD.setUpdatetime(new Date());
        Product productE = new Product();
        productE.setName("蓝澜 大理石茶几电视柜组合现代简约客厅不锈钢小户型功夫茶桌");
        productE.setImage("https://cdn.yikaidan.cn/jcstore/talkart/1480402228969_6642295.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402254393_67873025.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480402270440_57862000.jpg,https://cdn.yikaidan.cn/jcstore/talkart/1480930528666_50975555.jpg");
        productE.setCode("C72");
        productE.setStatus(1);
        productE.setStandard("1.3米茶几+2.0米电视柜");
        productE.setCostPrice(2099.0);
        productE.setSalePrice(3098.0);
        productE.setInventory(1740);
        productE.setShopId(shopId);
        productE.setUserId(user.getId());
        productE.setCustomDescription("{\"customDescription\":[{\"name\":\"款式定位\",\"info\":\"品质奢华型\"}]}");
        productE.setCreatetime(new Date());
        productE.setUpdatetime(new Date());
        List<Product> list = new ArrayList<Product>();
        list.add(productA);
        list.add(productB);
        list.add(productC);
        list.add(productD);
        list.add(productE);
        // 创建数据
        productMapper.insertBatch(list);
//        productMapper.generateDefaultProduct(shopId, user.getId());
    }

    public void deleteAllProduct(Shop shop) {
        // 删除全部商品
        ProductExample example = new ProductExample();
        example.createCriteria().andShopIdEqualTo(shop.getId());
        this.productMapper.deleteByExample(example);
    }

    /**
     * 为wap端查询商品
     *
     * @param shopId
     * @param groupId
     * @param currentPage
     * @param limit
     * @param orderType
     * @return
     */
    public Page<ProductBean> findProductForWap(Integer shopId, Integer groupId, Integer currentPage, Integer limit, Integer orderType) {
        ProductExample productExample = new ProductExample();
        // 排序方式
        if (orderType == 1) {
            productExample.setOrderByClause(" sale_price desc");
        } else if (orderType == 2) {
            productExample.setOrderByClause(" sale_price asc");
        } else {
            productExample.setOrderByClause(" updatetime desc");
        }
        Criteria criteria = productExample.createCriteria();
        // 商品属于当前店铺并且未被删除
        criteria.andShopIdEqualTo(shopId).andStatusEqualTo(ProductStatusConstant.Yes);
        if (groupId != null) {
            criteria.andGroupIdEqualTo(groupId);
        }
        // 分页查询
        PageHelper.startPage(currentPage, limit);
        return convertToBean((Page<Product>) productMapper.selectByExample(productExample));
    }

    /**
     * 添加客户案例
     *
     * @param productId
     * @param images
     * @param loginUser
     * @return
     */
    public ResultData saveCustomerCase(Integer productId, String[] images, User loginUser) {
        // 查询对应商品是否存在
        Product selectByPrimaryKey = productMapper.selectByPrimaryKey(productId);
        if (selectByPrimaryKey.getShopId().equals(shopService.findShop(loginUser).getId())) {
            Product product = new Product();
            product.setId(productId);
            if (images == null || images.length == 0) {
                product.setCustomerCase("");
            } else if (images.length <= 50) {
                StringBuilder builder = new StringBuilder();
                Arrays.asList(images).forEach(image -> builder.append(image).append(","));
                builder.deleteCharAt(builder.length() - 1);
                product.setCustomerCase(builder.toString());
            } else
                // 图片数量超出
                return ResultData.build().setStatus(Status.IMAGE_NUM_BEYOND);
            // 更新
            productMapper.updateByPrimaryKeySelective(product);
            return ResultData.build().success();
        }
        return ResultData.build().data404();
    }
    
    /**
     * 更改销量
     *
     * @param pid
     * @param inventory
     * @return
     */
    public int updateSalesVolume(Integer pid, Integer shopId, Integer inventory) {
        return productMapper.updateSalesVolume(pid, shopId, inventory);
    }
}
