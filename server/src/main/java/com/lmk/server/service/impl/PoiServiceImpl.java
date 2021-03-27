package com.lmk.server.service.impl;

import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/26
 */
@Service
public class PoiServiceImpl {

    @Resource
    private SysUserService sysUserService;

    public Workbook downLoadSysUserExcel() {
        //mybatis-plus自带的list查询所有
        List<SysUserEntity> list = sysUserService.list();
        //创建表Workbook
        Workbook workbook = new HSSFWorkbook();

        if (CollectionUtils.isNotEmpty(list)) {
            //创建工作博
            Sheet sheet = workbook.createSheet("用户1");

            //创建表头,创建第一行
            Row row0 = sheet.createRow(0);
//        第一行单元格的数据
            row0.createCell(0).setCellValue("姓名");
            row0.createCell(1).setCellValue("昵称");
            row0.createCell(2).setCellValue("邮箱");
            row0.createCell(3).setCellValue("状态");
            row0.createCell(4).setCellValue("创建时间");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (CollectionUtils.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    SysUserEntity user = list.get(i);
                    Row row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(StringUtils.isEmpty(user.getUsername()) ? "" : user.getUsername());
                    row.createCell(1).setCellValue(StringUtils.isEmpty(user.getName()) ? "" : user.getName());
                    row.createCell(2).setCellValue(StringUtils.isEmpty(user.getEmail()) ? "" : user.getEmail());
                    row.createCell(3).setCellValue(list.get(i).getStatus().equals(1) ? "启用" : "禁用");
                    row.createCell(4).setCellValue(sdf.format(list.get(i).getCreateTime()));
                }
            }
        }
        return workbook;
    }

    /*
     * @description 分页，分多个表，导出Excel表
     * @author lmk
     */
    public Workbook downLoadSysUserExcel2() {
        List<SysUserEntity> list = sysUserService.list();
        //判断list是否为空
        if (CollectionUtils.isEmpty(list)) {
            return new HSSFWorkbook();
        }
        int count = list.size();
        // 每一页5条数据,
        int sheetSize = 5;
        //工作薄的数量
        int sheect = count % sheetSize > 0 ? count / sheetSize + 1 : count / sheetSize;

        //算法：
        int start = 0;
        int end = sheetSize;

        //创建工作簿
        Workbook workbook = new HSSFWorkbook();
        for (int i = 0; i < sheect; i++) {
            //创建工作博
            Sheet sheet = workbook.createSheet("用户" + (i + 1));
            //创建表头,创建第一行
            Row row0 = sheet.createRow(0);
            // 第一行单元格的数据
            row0.createCell(0).setCellValue("姓名");
            row0.createCell(1).setCellValue("昵称");
            row0.createCell(2).setCellValue("邮箱");
            row0.createCell(3).setCellValue("状态");
            row0.createCell(4).setCellValue("创建时间");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int page = 1;
            //算法：
//            0-4,5-9
            for (int j = start; j < end; j++) {
                //获取list中的值
                SysUserEntity user = list.get(j);
                //每次循环五条
                Row row = sheet.createRow(page++);
                row.createCell(0).setCellValue(StringUtils.isEmpty(user.getUsername()) ? "" : user.getUsername());
                row.createCell(1).setCellValue(StringUtils.isEmpty(user.getName()) ? "" : user.getName());
                row.createCell(2).setCellValue(StringUtils.isEmpty(user.getEmail()) ? "" : user.getEmail());
                row.createCell(3).setCellValue(list.get(i).getStatus().equals(1) ? "启用" : "禁用");
                row.createCell(4).setCellValue(sdf.format(list.get(i).getCreateTime()));
            }

            start += sheetSize;
            end += sheetSize;
            //关键点是在这里。算法的妙处
            if (end >= count) {
                end = count;
            }
        }
        return workbook;
    }
}
