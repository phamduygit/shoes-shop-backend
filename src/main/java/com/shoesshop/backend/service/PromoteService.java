package com.shoesshop.backend.service;

import com.shoesshop.backend.entity.Promote;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.PromoteRepository;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PromoteService {
    private final PromoteRepository promoteRepository;

    public Promote save(Promote promote) {
        return promoteRepository.save(promote);
    }

    public Map<String, Object> getAllPromote() {
        List<Promote> promotes = promoteRepository.findAll();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("length", promotes.size());
        result.put("data", promotes);
        return result;
    }

    public Promote getPromote(int id) {
        return promoteRepository.findById(id).orElseThrow(() -> new NotFoundException("Promote doesn't exists by id: " + id));
    }

    public Promote updatePromote(int id, Promote promote) {
        if(!promoteRepository.existsById(id)) {
            throw new NotFoundException("Promote doesn't exists by id: " + id);
        }
        promote.setId(id);
        return promoteRepository.save(promote);
    }

    public void deletePromote(int id) {
        if(!promoteRepository.existsById(id)) {
            throw new NotFoundException("Promote doesn't exists by id: " + id);
        }
        promoteRepository.deleteById(id);
    }
}
