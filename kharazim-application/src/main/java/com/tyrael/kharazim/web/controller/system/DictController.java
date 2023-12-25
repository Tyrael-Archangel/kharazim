package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.system.dto.dict.*;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.common.dto.DataResponse;
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

import java.util.List;
import java.util.stream.Stream;

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

    @GetMapping("/types/pages")
    @Operation(summary = "字典分页查询")
    public PageResponse<DictDTO> pageDict(@ParameterObject PageDictRequest pageDictRequest) {
        return dictService.pageDict(pageDictRequest);
    }

    @GetMapping("/types/{id}/form")
    @Operation(summary = "字典详情")
    public DataResponse<DictDTO> dictDetail(@PathVariable("id") @Parameter(description = "字典编码ID", required = true) Long id) {
        return DataResponse.ok(dictService.dictDetail(id));
    }

    @PostMapping("/types")
    @Operation(summary = "新增字典")
    public Response addDict(@RequestBody @Valid SaveDictRequest addDictRequest,
                            @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        dictService.addDict(addDictRequest, currentUser);
        return Response.success();
    }

    @PutMapping("/types/{dictId}")
    @Operation(summary = "修改字典")
    public Response modify(@PathVariable("dictId") @Parameter(description = "字典编码ID", required = true) Long dictId,
                           @RequestBody @Valid SaveDictRequest modifyDictRequest,
                           @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        dictService.modify(dictId, modifyDictRequest, currentUser);
        return Response.success();
    }

    @DeleteMapping("/types/{dictIds}")
    @Operation(summary = "删除字典")
    public Response delete(@PathVariable("dictIds") @Parameter(description = "字典编码ID，多个以英文逗号(,)分割", required = true) String dictIds) {
        List<Long> dictIdValues = Stream.of(dictIds.split(","))
                .map(Long::parseLong)
                .toList();
        dictService.delete(dictIdValues);
        return Response.success();
    }

    @GetMapping("/types/{dictCode}/items")
    @Operation(summary = "字典项列表", description = "根据字典编码查询字典项列表")
    public MultiResponse<DictItemOptionDTO> listDictItem(@PathVariable("dictCode") @Parameter(description = "字典编码", required = true) String dictCode) {
        return dictService.listDictItem(dictCode);
    }

    @GetMapping("/items/pages")
    @Operation(summary = "字典项分页")
    public PageResponse<DictItemDTO> pageDictItem(@ParameterObject PageDictItemRequest pageDictItemRequest) {
        return dictService.pageDictItem(pageDictItemRequest);
    }

    @GetMapping("/items/{dictItemId}/form")
    @Operation(summary = "字典项详情")
    public DataResponse<DictItemDTO> dictItemDetail(@Parameter(description = "字典项ID", required = true) @PathVariable("dictItemId") Long dictItemId) {
        return DataResponse.ok(dictService.dictItemDetail(dictItemId));
    }

    @PostMapping("/items")
    @Operation(summary = "添加字典项")
    public Response addDictItem(@RequestBody @Valid SaveDictItemRequest addDictItemRequest,
                                @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        dictService.addDictItem(addDictItemRequest, currentUser);
        return Response.success();
    }

    @PutMapping("/items/{dictItemId}")
    @Operation(summary = "修改字典项")
    public Response modifyDictItem(@Parameter(description = "字典项ID", required = true) @PathVariable("dictItemId") Long dictItemId,
                                   @RequestBody @Valid SaveDictItemRequest modifyDictItemRequest,
                                   @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        dictService.modifyDictItem(dictItemId, modifyDictItemRequest, currentUser);
        return Response.success();
    }

    @DeleteMapping("/item/{dictItemIds}")
    @Operation(summary = "删除字典项")
    public Response deleteItem(@PathVariable("dictItemIds") @Parameter(description = "字典项ID", required = true) String dictItemIds,
                               @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        List<Long> dictItemIdValues = Stream.of(dictItemIds.split(","))
                .map(Long::parseLong)
                .toList();
        dictService.deleteItem(dictItemIdValues, currentUser);
        return Response.success();
    }

}
