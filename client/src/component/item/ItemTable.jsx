import ItemTableRow from "./ItemTableRow";
import PropTypes from 'prop-types';

function ItemTable({detailValues}){
    return(
        <table className="product-detail-table">
            <tbody>
                {detailValues.map(detailValue => <ItemTableRow key={detailValue.detail} productDetailName={detailValue.detail} productDetailValue={detailValue.value}/>)}
            </tbody>
        </table>
    )
}

ItemTable.propTypes = {
    detailValues: PropTypes.object
}

export default ItemTable;