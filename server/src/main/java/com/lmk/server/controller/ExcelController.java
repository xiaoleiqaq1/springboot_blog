package com.lmk.server.controller;

import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.service.impl.PoiServiceImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @auth: lmk
 * @Description: 导出excel文件
 * @date: 2021/3/26
 */
@RestController
@RequestMapping("excel")
public class ExcelController {
    @Resource
    private PoiServiceImpl poiService;

    private Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @RequestMapping(value = "downLoadSysUserExcel", method = RequestMethod.GET)
    public Result downLoadSysUserExcel(HttpServletResponse response) {
        Result result = new Result(StatusEnum.SUCCESS);
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("导出用户数据.xls".getBytes("utf-8"), "iso-8859-1"));
            response.setContentType("application/ynd.ms-excel;charset=UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("用户Execl数据错误",e);
            result=new Result(StatusEnum.FAIL);
        }

        try {
//            Workbook workbook = poiService.downLoadSysUserExcel();
            Workbook workbook = poiService.downLoadSysUserExcel2();
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            return null;
        } catch (Exception e) {
            logger.error("查询所有博客错误", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }


}
