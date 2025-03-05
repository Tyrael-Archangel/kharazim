package com.tyrael.kharazim.basicdata.controller.dict;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.basicdata.app.dto.dict.DictDTO;
import com.tyrael.kharazim.basicdata.app.dto.dict.DictItemDTO;
import com.tyrael.kharazim.basicdata.app.dto.dict.PageDictRequest;
import com.tyrael.kharazim.basicdata.app.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.basicdata.app.service.dict.DictService;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
@Tag(name = "字典值管理")
public class DictController {

    private final DictService dictService;

    @GetMapping("/page")
    @Operation(summary = "字典分页查询")
    public PageResponse<DictDTO> pageDict(@ParameterObject PageDictRequest pageDictRequest) {
        return dictService.pageDict(pageDictRequest);
    }

    @GetMapping("/{code}/items")
    @Operation(summary = "字典项列表", description = "根据字典编码查询字典项列表")
    public MultiResponse<DictItemDTO> listItems(
            @PathVariable("code") @Parameter(description = "字典编码", required = true) String code) {
        return MultiResponse.success(dictService.listItems(code));
    }

    @PostMapping("/item")
    @Operation(summary = "添加字典项")
    public DataResponse<Long> addDictItem(@RequestBody @Valid SaveDictItemRequest addDictItemRequest) {
        return DataResponse.success(dictService.addDictItem(addDictItemRequest));
    }

    @PutMapping("/item/{itemId}")
    @Operation(summary = "修改字典项")
    public Response modifyDictItem(@Parameter(description = "字典项ID", required = true) @PathVariable("itemId") Long itemId,
                                   @RequestBody @Valid SaveDictItemRequest modifyDictItemRequest,
                                   @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        dictService.modifyDictItem(itemId, modifyDictItemRequest, currentUser);
        return Response.success();
    }

}
