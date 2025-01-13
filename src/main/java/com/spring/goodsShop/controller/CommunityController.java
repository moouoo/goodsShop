package com.spring.goodsShop.controller;

import com.spring.goodsShop.etc.NavbarHelper;
import com.spring.goodsShop.etc.PageProcess;
import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.vo.NoticeVo;
import com.spring.goodsShop.vo.PageVo;
import com.spring.goodsShop.vo.ProductVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/community")
@Controller
public class CommunityController {
    @Autowired
    NavbarHelper navbarHelper;

    @Autowired
    AdminService adminService;

    @Autowired
    PageProcess pageProcess;

    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    String notice(Model model,
                  @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                  @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                  ){
        String part = "notice";
        List<ProductVo> empty = new ArrayList<>();
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, empty, "");
        List<NoticeVo> noticeList = adminService.getNoticeAllPagination(pageVo.getStartIndexNum(), pageVo.getOnePageCount());

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("pageVo", pageVo);

        navbarHelper.navbarSetup(model);
        return "/community/notice";
    }

    @RequestMapping(value = "/noticeDetail/{noticeId}", method = RequestMethod.GET)
    String noticeDetail(Model model, @PathVariable int noticeId, HttpSession session){
        if(noticeId <= 0){
            throw new IllegalArgumentException("noticeId가 0보다 작아서 오류.");
        }

        Object strLevel_obj = session.getAttribute("sLevel");
        if(strLevel_obj == null){
            return "redirect:/message/noLogin";
        }
        String strLevel = strLevel_obj.toString();

        int level = Integer.parseInt(strLevel);
        int navX = 1; // 관리자 계정으로 들어갈시 nav안보이게 설정.(navX == 1)

        if(!(level == 1 || level == 2 || level == 99)) return "redirect:/message/goMain";
        else if(level != 99) navX = 0;

        try {
            NoticeVo noticeVo = adminService.getNoticeByNoticeId(noticeId);
            model.addAttribute("vo", noticeVo);
        }
        catch (DataAccessException e) {
            System.out.println("notice 데이터를 받아오는 도중에 에러발생");
        }
        model.addAttribute("nav", navX);

        // 조회수처리
        String mid = session.getAttribute("sMid").toString();
        if(mid == null) return "redirect:/message/goMain";

        List<String> contentIds = (ArrayList) session.getAttribute("sContentIds");
        if(contentIds == null){
            contentIds = new ArrayList<>();
        }

        String contentIdsTem = "notice" + noticeId;
        if(!contentIds.contains(contentIdsTem)){
            adminService.updateNoticeCount(noticeId); // 1 증가시키기
            contentIds.add(contentIdsTem);
        }
        session.setAttribute("sContentIds", contentIds);

        navbarHelper.navbarSetup(model);
        return "/community/noticeDetail";
    }
}
