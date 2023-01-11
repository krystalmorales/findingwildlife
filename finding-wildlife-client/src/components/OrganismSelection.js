
function OrganismSelection({ organism }){

    return(
      <option value={organism.organism.organismId}>{organism.organism.commonName}</option>
  );

}

export default OrganismSelection;