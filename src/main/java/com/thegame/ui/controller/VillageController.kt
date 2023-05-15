package com.thegame.ui.controller

import com.thegame.business.service.BuildingService
import com.thegame.business.service.ResourceService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDateTime

@Controller
class VillageController(private  val resourceService: ResourceService,
                        private val buildingService: BuildingService) {

    @RequestMapping("village.html")
    fun showVillage(model: Model):String {

        //ToDo: Anpassen - Perspektivisch soll hier Village mit variabler villageId angezeigt werden
        model.addAttribute("resources", resourceService.getResourcesByVillageId(1L))
        model.addAttribute("buildings", buildingService.getBuildingsByVillageId(1L))
        model.addAttribute("timestampNow", LocalDateTime.now())
        return "village"
    }
}