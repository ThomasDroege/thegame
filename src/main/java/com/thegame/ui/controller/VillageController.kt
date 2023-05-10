package com.thegame.ui.controller

import com.thegame.business.model.Resource
import com.thegame.business.service.ResourceService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class VillageController(private  val resourceService: ResourceService) {

    @RequestMapping("village.html")
    fun showVillage(model: Model) {

        //ToDo: Anpassen - Es sollen hiermit alle Resourcen des Village mit id=1 angezeigt werden
        // val resources: List<Resource> =  resourceService.getResourcesByVillageId(1L)

        val resources: List<Resource> = resourceService.getAllResources()
        val food = resources[0]
        model.addAttribute("food", food)

    }
}