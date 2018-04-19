

public class Connect4World implements DomainGenerator {
	// generateDomain()
	// returns newly instanced Domain object
	
	public SADomain generateDomain() {
		SADomain domain = new SADomain();
		
		domain.addActionType(new MoveActionType());
		return domain;
	}

}
