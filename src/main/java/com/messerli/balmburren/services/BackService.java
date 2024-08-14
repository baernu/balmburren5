package com.messerli.balmburren.services;



import com.messerli.balmburren.entities.Back;
import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.User;

import java.util.List;
import java.util.Optional;

public interface BackService {
    public Optional<Back> saveBack(Back back);
    public Optional<Back> putBack(Back back);
    public Optional<Back> deleteBack(User user, ProductBindProductDetails productAndInfo, Dates date);
    public Optional<Back> getBack(User user, ProductBindProductDetails productAndInfo, Dates date);
    public Optional<List<Back>> getAllBackForPeople(User user);
}
