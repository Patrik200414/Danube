function changeProductDetail(value, detailId, details){
    const changedDetails = details.map(detail => {
        if(detail.id === detailId){
            return{
                ...detail,
                value: value
            }
        }
        return detail;
    });
    return changedDetails;
}

export default changeProductDetail;