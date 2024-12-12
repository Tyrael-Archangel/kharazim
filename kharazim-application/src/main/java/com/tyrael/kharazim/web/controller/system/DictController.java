package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.system.dto.dict.DictDTO;
import com.tyrael.kharazim.application.system.dto.dict.DictItemDTO;
import com.tyrael.kharazim.application.system.dto.dict.PageDictRequest;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
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
    public Response addDictItem(@RequestBody @Valid SaveDictItemRequest addDictItemRequest,
                                @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        dictService.addDictItem(addDictItemRequest, currentUser);
        return Response.success();
    }

    @PutMapping("/item/{itemId}")
    @Operation(summary = "修改字典项")
    public Response modifyDictItem(@Parameter(description = "字典项ID", required = true) @PathVariable("itemId") Long itemId,
                                   @RequestBody @Valid SaveDictItemRequest modifyDictItemRequest,
                                   @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        dictService.modifyDictItem(itemId, modifyDictItemRequest, currentUser);
        return Response.success();
    }

}
