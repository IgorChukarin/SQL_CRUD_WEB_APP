package com.example.clothingShop;
import com.example.clothingShop.domain.Good;
import com.example.clothingShop.repos.GoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GreetingController {


    @Autowired
    private GoodRepo goodRepo;


    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }


    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Good> goods = goodRepo.findAll();
        model.put("message", "hello!");
        model.put("goods", goods);
        return "main";
    }


    @PostMapping("add")
    public String add(@RequestParam String name,
                      @RequestParam Integer categoryId,
                      @RequestParam String size,
                      @RequestParam Integer count,
                      @RequestParam Integer price,
                      Map<String, Object> model) {

        ArrayList<Integer> clothesIdList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 7, 8, 9));
        ArrayList<Integer> shoesIdList = new ArrayList<>(Arrays.asList(5, 6));
        ArrayList<String> clothesSizes = new ArrayList<>(Arrays.asList("S", "M", "L", "XL"));
        ArrayList<String> shoesSizes = new ArrayList<>(Arrays.asList("38", "39", "40", "41"));


        if (name == null || categoryId == null || size == null || count == null || price == null) {
            Iterable<Good> goods = goodRepo.findAll();
            model.put("message", "not all fields are filled in");
            model.put("goods", goods);
            return "main";
        }

        if (clothesIdList.contains(categoryId) && !clothesSizes.contains(size)) {
            Iterable<Good> goods = goodRepo.findAll();
            model.put("message", "wrong size type for clothes");
            model.put("goods", goods);
            return "main";
        }

        if (shoesIdList.contains(categoryId) && !shoesSizes.contains(size)) {
            Iterable<Good> goods = goodRepo.findAll();
            model.put("message", "wrong size type for shoes");
            model.put("goods", goods);
            return "main";
        }

        System.out.println(size);
        if (categoryId == 10 && !size.equals("one_size")) {
            Iterable<Good> goods = goodRepo.findAll();
            model.put("message", "wrong size type for headdress");
            model.put("goods", goods);
            return "main";
        }

        Good good = new Good(name, categoryId, size, count, price);
        goodRepo.save(good);
        Iterable<Good> goods = goodRepo.findAll();
        model.put("message", "record added");
        model.put("goods", goods);
        return "main";
    }

    @PostMapping("delete")
    public String delete(@RequestParam Integer id, Map<String, Object> model) {
        String message;
        if (id == null) {
            message = "id is not selected";
        } else {
            goodRepo.deleteById(id);
            message = "record deleted";
        }

        Iterable<Good> goods = goodRepo.findAll();
        
        model.put("message", message);
        model.put("goods", goods);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam Integer from, @RequestParam Integer to, Map<String, Object> model) {
        String message;
        if (from == null && to == null) {
            message = "no filters set";
        }else if (from != null && to == null) {
            message = "from " + from;
        }else if (from == null && to != null) {
            message = "up to " + to;
        }else {
            message = "from " + from + " to " + to;
        }

        Iterable<Good> goods = goodRepo.findAll();
        List<Good> filteredGoods = filterGoods(from, to, (ArrayList) goods);

        model.put("message", message);
        model.put("goods", filteredGoods);
        return "main";
    }


    @PostMapping("update")
    public String update(@RequestParam Integer id, @RequestParam String name, Map<String, Object> model) {
        Iterable<Good> goods = goodRepo.findAll();

        String message;
        if (id == null || name == null) {
            message = "not all fields are filled in";
        }
        else {
            message = "record updated";
            goodRepo.findById(id).get().setName(name);
            goodRepo.saveAll(goods);
        }
        model.put("message", message);
        model.put("goods", goods);
        return "main";
    }


    private ArrayList filterGoods(Integer priceFrom, Integer priceTo, ArrayList<Good> listOfGoods) {
        if (priceFrom == null && priceTo == null) {
            return listOfGoods;
        }
        else if (priceFrom != null && priceTo == null) {
            return (ArrayList) listOfGoods.stream().filter(good -> priceFrom <= good.getPrice())
                                          .collect(Collectors.toList());
        }
        else if (priceFrom == null && priceTo != null) {
            return (ArrayList) listOfGoods.stream().filter(good -> good.getPrice() <= priceTo)
                                          .collect(Collectors.toList());
        }
        else {
            return (ArrayList) listOfGoods.stream().filter(good -> priceFrom <= good.getPrice() && good.getPrice() <= priceTo)
                                          .collect(Collectors.toList());
        }
    }
}
