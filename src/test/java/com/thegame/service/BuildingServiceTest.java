package com.thegame.service;

import com.thegame.business.repository.BuildingRepository;
import com.thegame.business.service.BuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
@ExtendWith(MockitoExtension.class)
class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private BuildingService buildingService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetBuildingsByVillageId() {
        Long villageId = 1L;

        BuildingRepository.BuildingsByVillageIdResponse mockBuilding = mock(BuildingRepository.BuildingsByVillageIdResponse.class);
        when(buildingRepository.getBuildingsByVillageId(villageId))
                .thenReturn(List.of(mockBuilding, mockBuilding, mockBuilding));

        List<BuildingRepository.BuildingsByVillageIdResponse> result = buildingService.getBuildingsByVillageId(villageId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        verify(buildingRepository).getBuildingsByVillageId(villageId); // Verify whether repository call is made
    }
}*/