package com.thegame.ui.controller

import com.thegame.business.repository.ResourceRepository
import com.thegame.business.service.ResourceService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class VillageController(private  val resourceService: ResourceService) {

    @RequestMapping("village.html")
    fun showVillage(model: Model):String {

        //ToDo: Anpassen - Perspektivisch soll hier Village mit variabler villageId angezeigt werden
        val resources: List<ResourceRepository.ResourceByVillageResponse> =  resourceService.getResourcesByVillageId(1L)
        model.addAttribute("resources", resources)
        return "village"
    }
}