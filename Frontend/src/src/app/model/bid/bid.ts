import {PersonaViewModel} from "../persona/persona-view-model";

export interface Bid {
  id?: number;
  offerId: number;
  userEmail: string;
  persona: PersonaViewModel;
  freeProductSample: boolean;
  acceptInitialRequirements: boolean;
  compensation?: string;
}
