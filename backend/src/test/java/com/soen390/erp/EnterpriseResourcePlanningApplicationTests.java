//package com.soen390.erp;
//
//import com.soen390.erp.accounting.controller.SupplierController;
//import com.soen390.erp.accounting.model.Supplier;
//import com.soen390.erp.accounting.repository.SupplierRepository;
//import com.soen390.erp.accounting.service.SupplierService;
//import com.soen390.erp.inventory.model.Plant;
//
//import com.soen390.erp.inventory.repository.PlantPartRepository;
//import com.soen390.erp.inventory.repository.PlantRepository;
//import com.soen390.erp.inventory.service.PlantService;
//import com.soen390.erp.manufacturing.model.*;
//import com.soen390.erp.manufacturing.repository.BikeRepository;
//import com.soen390.erp.manufacturing.repository.MaterialRepository;
//import com.soen390.erp.manufacturing.repository.PartRepository;
//import com.soen390.erp.inventory.repository.PlantMaterialRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.doReturn;
//
//@SpringBootTest
//class EnterpriseResourcePlanningApplicationTests{
////
////    @Autowired
////    private MaterialRepository materialRepository;
////    @Autowired
////    private PartRepository partRepository;
////    @Autowired
////    private BikeRepository bikeRepository;
////    @Autowired
////    private PlantRepository plantRepository;
////    @Autowired
////    PlantMaterialRepository plantMaterialRepository;
////    @Autowired
////    PlantService plantService;
////    @Autowired
////    PlantPartRepository plantPartRepository;
////
////    @Autowired
////    public EnterpriseResourcePlanningApplicationTests(MaterialRepository materialRepository, PartRepository partRepository, BikeRepository bikeRepository, PlantRepository plantRepository) {
//////        this.materialRepository = materialRepository;
//////        this.partRepository = partRepository;
//////        this.bikeRepository = bikeRepository;
//////        this.plantRepository = plantRepository;
//////    }
////
////
//
////    @Autowired
////    private SupplierController supplierController;
////
////    @Autowired
////    private SupplierService supplierService;
////
////    @MockBean
////    SupplierRepository supplierRepository;
//
//
//    @Test
//    void contextLoads() {
//
//    }
//
////    @Test
////    public void getAllSuppliersTest()
////    {
////        Supplier s1 = new Supplier();
////        Supplier s2 = new Supplier();
////
////        List<Supplier> suppliers = new ArrayList<>();
////        suppliers.add(s1);
////        suppliers.add(s2);
////
//////        when(supplierService.findAllSuppliers()).thenReturn(suppliers);
//////        doReturn(suppliers).when(supplierService).findAllSuppliers();
////        doReturn(suppliers).when(supplierRepository).findAll();
////
////
////        List<Supplier> result = supplierController.getAllSuppliers();
////        for (Supplier s : result ) {
////            System.out.println(s);
////        }
////
////        assertThat(result.size()).isEqualTo(2);
////    }
//
//
////
////
//////    @Test
//////    void delete(){
//////
//////        partRepository.deleteById(106);
//////    }
////
//////    @Test
//////    void testPlant(){
////////        Material m1 = materialRepository.findAll().get(1);
//////
//////        Material m1 = materialRepository.findById(54).orElseGet(()->Material.builder().name("didn't find").cost(10).build());
////////        m1.setName("test");
////////        m1.setCost(21);
////////        materialRepository.save(m1);
////////        PlantMaterial pm = PlantMaterial.builder().material(m1).quantity(12).build();
////////        PlantMaterial pm = plantMaterialRepository.findByMaterial(m1)
////////                .orElseGet(()->PlantMaterial.builder().material(m1).build());
////////        pm.setQuantity(24);
////////        plantMaterialRepository.save(pm);
//////        Plant plant = plantRepository.findById(1).orElse(new Plant());
////////        plant.setName("Plant1");
////////        plant.setAddress("123 Street");
//////
//////        plantService.addPlantMaterial(plant, m1, 4);
////////        plantRepository.save(plant);
//////
//////    }
//////
//////
//////
//////    @Test
//////    void testInventory(){
//////        Plant plant = plantRepository.findById(1).orElse(new Plant());
//////        Part part = partRepository.findById(48).orElseGet(()->new Part());
//////
//////        Material material = materialRepository.findById(54).orElseGet(() -> new Material());
//////        part.addMaterial(material);
//////
//////        plantService.addPlantPart(plant, part, 5);
//////    }
//////
//////    //For development testing only
//////    @Test
//////    void testParts() {
//////
//////        Material m1 = materialRepository.findById(1).orElseGet(() -> Material.builder().name("ugh").cost(20).build());
//////        m1.setName("temp");
//////        m1.setCost(10);
//////        materialRepository.save(m1);
//////    }
//////
//////    @Test
//////    void testBikePlant(){
//////        Bike bike = bikeRepository.findById(86).get();
//////        Plant plant = plantRepository.findById(1).get();
//////
//////        Set<Part> bikeParts = Set.of(bike.getFrame(), bike.getFrontwheel(), bike.getHandlebar(), bike.getRearwheel(),
//////                bike.getPedal(), bike.getSeat());
//////
//////        bikeParts.forEach(part -> plantService.addPlantPart(plant, part, 10));
//////
//////        plantService.addPlantBike(plant, bike, 1);
//////
//////    }
//////
////////
//////////
////////    Wheel wheel = new Wheel();
////////    wheel.setName("frontwheel121");
////////    wheel.setCost(32);
////////    wheel.setDiameter(2);
////////    wheel.setGear(false);
////////    wheel.addMaterial(m1);
////////    partRepository.save(wheel);
//////////
////////    Handlebar handlebar = Handlebar.builder().build();
////////    Wheel rearWheel = Wheel.builder().build();
////////    Seat seat = Seat.builder().build();
////////    Pedal pedal = Pedal.builder().build();
////////    Frame frame = Frame.builder().build();
////////
////////    partRepository.saveAll(Set.of(handlebar, rearWheel, seat, pedal, frame));
////////
////////
////////    Bike bike = Bike.builder()
////////            .name("Second_bike")
////////            .frontwheel(wheel)
////////            .rearwheel(rearWheel)
////////            .handlebar(handlebar)
////////            .seat(seat)
////////            .frame(frame)
////////            .pedal(pedal)
////////        .build();
////////
////////    bikeRepository.save(bike);
//////////
//////////    //TODO: CHECK IF THE ADDED THE SAME PARTS TO ANOTHER BIKE ARE BEING SAVED PROPERLY (possibly not needed)
////////    //TODO: CHECK ACCESSORIES BEING ADDED
////////    }
//////
////
//}
