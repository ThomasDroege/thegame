package com.thegame.controller

import com.thegame.model.Resource
import com.thegame.service.ResourceService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class VillageController(private  val resourceService: ResourceService) {

    @RequestMapping("village.html")
    fun showVillage(model: Model) {
        val resources: List<Resource> =  resourceService.getAllResources()
        val food = resources[0]
        model.addAttribute("food", food)
        //val resourcesWithVillageOne = resources.stream().filter{it -> it.village?.villageId == 1L }
        //val resourceTotalWithVillageOne = resources.stream().filter{it -> it.village?.villageId == 1L }.map{it -> it.resourceTotal}

    }

    companion object {
        var foodTotal: Long = 0L
        var foodInit: Long? = null
    }
}