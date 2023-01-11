
function ParkSelection({park}){

    return(
        <option value={park.parkId}>{park.parkName}</option>
    );
}

export default ParkSelection;