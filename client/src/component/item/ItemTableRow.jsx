import Proptypes from 'prop-types';

function ItemTableRow({productDetailName, productDetailValue}){
    return(
        <tr>
            <th>{productDetailName}</th>
            <td>{productDetailValue}</td>
        </tr>
    )
}

ItemTableRow.propTypes = {
    productDetailName: Proptypes.string,
    productDetailValue: Proptypes.string
}

export default ItemTableRow;