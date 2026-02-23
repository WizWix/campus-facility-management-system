package io.github.wizwix.cfms.service;

import io.github.wizwix.cfms.service.iface.IClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService implements IClubService {}
