package com.example.url_view;

import com.example.url_profile.UrlProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "url_views")
public class UrlView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "url_id")
    private UrlProfile urlProfile;

    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "os_system")
    private String osSystem;
    private String browser;
    private String referer;

    public UrlView(UrlProfile urlProfile, String ipAddress, String osSystem, String browser, String referer) {
        this.urlProfile = urlProfile;
        this.ipAddress = ipAddress;
        this.osSystem = osSystem;
        this.browser = browser;
        this.referer = referer;
    }
}
