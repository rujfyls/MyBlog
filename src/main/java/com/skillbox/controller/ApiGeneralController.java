package com.skillbox.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.skillbox.controller.dto.request.EnteredDataForEditProfileRequestDTO;
import com.skillbox.controller.dto.request.ProfileRequestDTO;
import com.skillbox.controller.dto.request.SettingsRequestDTO;
import com.skillbox.controller.dto.response.*;
import com.skillbox.entity.User;
import com.skillbox.service.PostService;
import com.skillbox.service.SettingService;
import com.skillbox.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponseDTO initResponseDTO;
    private final SettingService settingService;
    private final UserService userService;
    private final PostService postService;
    private final Cloudinary cloudinaryConfig;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ApiGeneralController(InitResponseDTO initResponseDTO,
                                SettingService settingService,
                                UserService userService,
                                PostService postService,
                                Cloudinary cloudinaryConfig) {
        this.initResponseDTO = initResponseDTO;
        this.settingService = settingService;
        this.userService = userService;
        this.postService = postService;
        this.cloudinaryConfig = cloudinaryConfig;
    }

    @GetMapping("/init")
    public ResponseEntity<InitResponseDTO> init() {
        return ResponseEntity.ok(initResponseDTO);
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingResponseDTO> settings() {
        SettingResponseDTO setting = new SettingResponseDTO();
        settingService.findAll().forEach(s -> {
            if (s.getCode().equals("MULTIUSER_MODE")) {
                setting.setMultiuserMode(s.getValue().equals("YES"));
            } else if (s.getCode().equals("POST_PREMODERATION")) {
                setting.setPostPremoderation(s.getValue().equals("YES"));
            } else if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
                setting.setStatisticsIsPublic(s.getValue().equals("YES"));
            }
        });
        return ResponseEntity.ok(setting);
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public void settings(@RequestBody SettingsRequestDTO settingsRequestDTO) {
        settingService.findAll().forEach(s -> {
            if (s.getCode().equals("MULTIUSER_MODE")) {
                s.setValue(settingsRequestDTO.getMultiuserMode() ? "YES" : "NO");
            } else if (s.getCode().equals("POST_PREMODERATION")) {
                s.setValue(settingsRequestDTO.getPostPremoderation() ? "YES" : "NO");
            } else if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
                s.setValue(settingsRequestDTO.getStatisticsIsPublic() ? "YES" : "NO");
            }
            settingService.save(s);
        });
    }

    @GetMapping("/statistics/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticsResponseDTO> myStatistics(Principal principal) {
        Integer userId = userService.getUserByEmail(principal.getName()).getUserId();
        StatisticsResponseDTO statistics = new StatisticsResponseDTO();

        statistics.setLikesCount(postService.getLikesCountByUserId(userId));
        statistics.setFirstPublication(postService.getFirstPublicationByUserId(userId));
        statistics.setPostsCount(postService.getPostsCountByUserId(userId));
        statistics.setViewsCount(postService.getViewsCountByUserId(userId));
        statistics.setDislikesCount(postService.getDislikesCountByUserId(userId));

        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponseDTO> allStatistics(Principal principal) {
        if (settingService.getSettingsByStatistics().getValue().equals("NO")) {
            if (principal == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            if (userService.getUserByEmail(principal.getName()).getIsModerator() != 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        List<Integer> listUsersId = userService.getListUsersId();
        StatisticsResponseDTO statistics = new StatisticsResponseDTO();
        List<Long> listFirstPublication = new ArrayList<>();
        List<Integer> likes = new ArrayList<>();
        List<Integer> posts = new ArrayList<>();
        List<Integer> views = new ArrayList<>();
        List<Integer> dislikes = new ArrayList<>();


        listUsersId.forEach(userId -> {
            likes.add(postService.getLikesCountByUserId(userId));
            posts.add(postService.getPostsCountByUserId(userId));
            views.add(postService.getViewsCountByUserId(userId));
            dislikes.add(postService.getDislikesCountByUserId(userId));
            listFirstPublication.add(postService.getFirstPublicationByUserId(userId));
        });

        if (!listFirstPublication.isEmpty()) {
            statistics.setFirstPublication(Collections.min(listFirstPublication));
        }

        statistics.setLikesCount(likes.stream().reduce(Integer::sum).get());
        statistics.setPostsCount(posts.stream().reduce(Integer::sum).get());
        statistics.setViewsCount(views.stream().reduce(Integer::sum).get());
        statistics.setDislikesCount(dislikes.stream().reduce(Integer::sum).get());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @RequestMapping(path = "/image", method = POST, consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<String> uploadImage(@RequestPart("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return new ResponseEntity<>(new ImageResponseDRO("Missing data").toString(),
                    HttpStatus.BAD_REQUEST);
        }
        if (image.getSize() > 5_242_880) {
            System.out.println(image.getSize());
            return new ResponseEntity<>(new ImageResponseDRO("The file size exceeds the allowed size").toString(),
                    HttpStatus.BAD_REQUEST);
        }
        if (!getImageFormat(image.getOriginalFilename()).equals("png") &&
                !getImageFormat(image.getOriginalFilename()).equals("jpg")) {
            return new ResponseEntity<>(new ImageResponseDRO("Invalid image format").toString(),
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(saveImage(image, null));
    }

    @RequestMapping(path = "/profile/my", method = POST, consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<EditProfileResponseDTO> editProfile(
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "removePhoto", required = false) Short removePhoto,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            Principal principal) throws IOException {

        User currentUser = userService.getUserByEmail(principal.getName());

        EnteredDataForEditProfileRequestDTO enteredData = userService.checkingEnteredDataForEditProfile(
                currentUser,
                name,
                email,
                password,
                photo);

        if (enteredData.checkingEnteredData()) {
            currentUser.setName(name);
            currentUser.setEmail(email);
            if (password != null) {
                currentUser.setPassword(encoder.encode(password));
            }
            if (photo != null) {
                saveImage(photo, currentUser);
            }
            if (removePhoto != null && removePhoto == 1) {
                currentUser.setPhoto("");
            }
            userService.save(currentUser);
        }

        return ResponseEntity.ok(new EditProfileResponseDTO(enteredData));
    }

    @RequestMapping(path = "/profile/my", method = POST, consumes = {"application/json"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<EditProfileResponseDTO> editProfile(@RequestBody ProfileRequestDTO profileRequestDTO,
                                                              Principal principal) {

        User currentUser = userService.getUserByEmail(principal.getName());

        EnteredDataForEditProfileRequestDTO enteredData = userService.checkingEnteredDataForEditProfile(
                currentUser,
                profileRequestDTO.getName(),
                profileRequestDTO.getEmail(),
                profileRequestDTO.getPassword(),
                null);

        if (enteredData.checkingEnteredData()) {
            currentUser.setName(profileRequestDTO.getName());
            currentUser.setEmail(profileRequestDTO.getEmail());
            if (profileRequestDTO.getPassword() != null) {
                currentUser.setPassword(encoder.encode(profileRequestDTO.getPassword()));
            }
            if (profileRequestDTO.getRemovePhoto() != null && profileRequestDTO.getRemovePhoto() == 1) {
                currentUser.setPhoto("");
            }
            userService.save(currentUser);
        }

        return ResponseEntity.ok(new EditProfileResponseDTO(enteredData));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, Integer width, Integer height) throws IOException {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private String getImageFormat(String imageName) {
        return imageName.substring(imageName.lastIndexOf('.') + 1);
    }

    private String saveImage(MultipartFile image, User user) throws IOException {
        BufferedImage bi = ImageIO.read(image.getInputStream());

        if (user != null) {
            bi = resizeImage(bi, 36, 36);
        } else if (bi.getWidth() > 400 || bi.getHeight() > 400) {
            bi = resizeImage(bi, 400, 400);
        }

        Path path = Paths.get("src/main/resources/upload/ab/cd/ef/");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(path.toAbsolutePath() + "/" + image.getOriginalFilename());
        ImageIO.write(bi, "png", file);

        Map uploadResult = cloudinaryConfig.uploader().upload(file, ObjectUtils.emptyMap());
        String url = uploadResult.get("url").toString();

        file.delete();

        if (user != null) {
            user.setPhoto(url);
            userService.save(user);
        }

        return url;
    }
}
