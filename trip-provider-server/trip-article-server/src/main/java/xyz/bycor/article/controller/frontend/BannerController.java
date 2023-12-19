package xyz.bycor.article.controller.frontend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.domain.Banner;
import xyz.bycor.article.service.BannerService;
import xyz.bycor.core.response.R;

import java.util.List;

/**
 * @author BAI
 */

@RestController
@RequestMapping("/banners")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/travel")
    public R<List> travel() {
      return   R.ok(bannerService.findBannerByType(Banner.TYPE_TRAVEL));
    }

    @GetMapping("/strategy")
    public R<List> strategy() {
        return   R.ok(bannerService.findBannerByType(Banner.TYPE_STRATEGY));
    }



}
