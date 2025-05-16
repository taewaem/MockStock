function getVolumeRank() {
    $.ajax({
        url: '/volume_rank',  // 외부 API 엔드포인트
        method: 'GET',
        async: false , // false: 비동기
        success: function(data) {
            // 응답에서 필요한 데이터를 추출
            console.log(data)
        }
    });
}

getVolumeRank();

$(document).ready(function() {
    $.getJSON("volume_rank", function(data) {
        // 할 일 처리
        let volume_data = "";

        // 'output' 배열을
        $.each(data.output, function(index, value) {
          if (index >= 10) return false; // 10개 이상일 경우 중단
        console.log(index);
            volume_data += "<tr>";
            volume_data += "<td>" + value.data_rank + "</td>";      // 랭크
            volume_data += "<td>" + value.hts_kor_isnm + "</td>";  // 종목명
            volume_data += "<td>" + Number(value.stck_prpr).toLocaleString() + '원' + "</td>";      // 주가

            // 전일대비
            let priceChangeClass = '';
            if (value.prdy_vrss.startsWith('-')) {
                priceChangeClass = 'blue';  // 파란색
            volume_data += `<td class="${priceChangeClass}">` + Number(value.prdy_vrss).toLocaleString() + "</td>";
            } else if (value.prdy_vrss.startsWith('0')) {
                priceChangeClass = 'black'; // 검정색
            volume_data += `<td class="${priceChangeClass}">` + Number(value.prdy_vrss).toLocaleString() + "</td>";
            } else {
                priceChangeClass = 'red';    // 빨간색
            volume_data += `<td class="${priceChangeClass}">` + '+' +Number(value.prdy_vrss).toLocaleString() + "</td>";
            }

            // 전일대비율
            let rateClass = '';
            if (value.prdy_vrss.startsWith('-')) {
                rateClass = 'blue';  // 파란색
            volume_data += `<td class="${rateClass}">` + Number(value.prdy_ctrt).toLocaleString() + "</td>";
            } else if (value.prdy_vrss.startsWith('0')) {
                rateClass = 'black'; // 검정색
            volume_data += `<td class="${rateClass}">` + Number(value.prdy_ctrt).toLocaleString() + "</td>";
            } else {
                rateClass = 'red';    // 빨간색
            volume_data += `<td class="${rateClass}">` + '+' +Number(value.prdy_ctrt).toLocaleString() + "</td>";
            }

            // 거래량
            volume_data += "</tr>";
        });

        // 테이블 본문에 추가 id랑 일치 시키기.
        $("#volume_rank_table").append(volume_data);
    });
});