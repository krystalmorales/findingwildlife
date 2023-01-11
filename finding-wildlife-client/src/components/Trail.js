function Trail({ trail }){

    return(
        <option value={trail.trailId}>{trail.trailName}</option>
    );
}

export default Trail;