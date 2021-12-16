package com.jungdam.album.presentation;

import com.jungdam.album.dto.bundle.CreateAlbumBundle;
import com.jungdam.album.dto.bundle.ReadOneAlbumBundle;
import com.jungdam.album.dto.request.CreateAlbumRequest;
import com.jungdam.album.dto.response.CreateAlbumResponse;
import com.jungdam.album.dto.response.ReadOneAlbumResponse;
import com.jungdam.album.facade.AlbumFacade;
import com.jungdam.common.dto.ResponseDto;
import com.jungdam.common.dto.ResponseMessage;
import com.jungdam.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Albums")
@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private final AlbumFacade albumFacade;

    public AlbumController(AlbumFacade albumFacade) {
        this.albumFacade = albumFacade;
    }

    @ApiOperation("앨범 생성")
    @PostMapping
    public ResponseEntity<ResponseDto<CreateAlbumResponse>> create(
        @RequestBody CreateAlbumRequest request) {
        Long memberId = SecurityUtils.getCurrentUsername();

        CreateAlbumBundle bundle = new CreateAlbumBundle(memberId, request);
        CreateAlbumResponse response = albumFacade.insert(bundle);

        return ResponseDto.of(ResponseMessage.ALBUM_CREATE_SUCCESS, response);
    }

    @ApiOperation("앨점 제목 및 가훈 조회")
    @GetMapping("/{albumId}")
    public ResponseEntity<ResponseDto<ReadOneAlbumResponse>> getOne(@PathVariable Long albumId) {
        Long memberId = SecurityUtils.getCurrentUsername();

        ReadOneAlbumBundle bundle = new ReadOneAlbumBundle(memberId, albumId);

        ReadOneAlbumResponse response = albumFacade.find(bundle);

        return ResponseDto.of(ResponseMessage.ALBUM_READ_SUCCESS, response);
    }
}